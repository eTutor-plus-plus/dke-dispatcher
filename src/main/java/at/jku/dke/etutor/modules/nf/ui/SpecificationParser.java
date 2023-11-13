package at.jku.dke.etutor.modules.nf.ui;

import at.jku.dke.etutor.modules.nf.*;
import at.jku.dke.etutor.modules.nf.algorithms.Closure;
import at.jku.dke.etutor.modules.nf.algorithms.MinimalCover;
import at.jku.dke.etutor.modules.nf.algorithms.ReductionByResolution;
import at.jku.dke.etutor.modules.nf.analysis.*;
import at.jku.dke.etutor.modules.nf.analysis.normalform.NormalformAnalysis;
import at.jku.dke.etutor.modules.nf.analysis.normalform.NormalformAnalyzer;
import at.jku.dke.etutor.modules.nf.analysis.normalform.NormalformViolation;
import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;
import at.jku.dke.etutor.modules.nf.model.Key;
import at.jku.dke.etutor.modules.nf.model.NormalformLevel;
import at.jku.dke.etutor.modules.nf.model.Relation;

import java.io.Serializable;
import java.util.*;

/**
 * Provides methods for parsing a text to build parts of an RDBD specification.
 * RDBD specifications can be entered textually by assistants, which makes the 
 * process of defining exercise specifications much easier. 
 * Each internal RDBD type provides its own special syntax.
 * 
 * @author Georg Nitsche (10.01.2006)
 *
 */
public class SpecificationParser implements Serializable {
	
	private static final long serialVersionUID = 3835519487979543103L;
	private static final String LINE_SEP = System.getProperty("line.separator", "\n");
	
	public final static String QUALIFIER_DEPENDENCIES = "1";
	public final static String QUALIFIER_ATTRIBUTES_BASE = "2";
	public final static String QUALIFIER_ATTRIBUTES_RELATION = "3";
	public final static String PATTERN_ATTRIBUTE = "\\s*\\w*\\s*";

	private final Map<String, String> qualifiers;
	private Set<String> baseAttributes;
	private Set<String> relationAttributes;
	private Set<FunctionalDependency> dependencies;
	
	private SpecificationParser() {
		this("R", "B", "F");
	}
	
	public SpecificationParser(String qualRelation, String qualBase, String qualDependencies) {
		super();
		reset();
		this.qualifiers = new HashMap<>();
		qualifiers.put(QUALIFIER_ATTRIBUTES_RELATION, qualRelation);
		qualifiers.put(QUALIFIER_ATTRIBUTES_BASE, qualBase);
		qualifiers.put(QUALIFIER_DEPENDENCIES, qualDependencies);
	}
	
	protected void reset() {
		this.baseAttributes = null;
		this.relationAttributes = null;
		this.dependencies = null;
	}
	
	public void parse(String txt) throws SpecificationParserException {
		String msg;
		int index;
		String qualifier;
		String body;
	
		reset();
		if (txt == null || txt.trim().isEmpty()) {
			return;
		}
		do {
			qualifier = null;
			body = null;
			index = txt.indexOf('{');
			if (index > -1) {
				qualifier = txt.substring(0, index).trim();
				validateQualifier(qualifier);
				txt = txt.substring(++index).trim();
				index = txt.indexOf('}');
				if (index > -1) {
					body = txt.substring(0, index).trim();
					if (qualifiers.get(QUALIFIER_DEPENDENCIES) != null && qualifiers.get(QUALIFIER_DEPENDENCIES).equals(qualifier)) {
						if (this.dependencies != null) {
							msg = "Duplicate dependencies specification " + qualifier;
							throw new SpecificationParserException(msg);		
						}
						this.dependencies = parseDependencies(body);
					} else if (qualifiers.get(QUALIFIER_ATTRIBUTES_RELATION) != null && qualifiers.get(QUALIFIER_ATTRIBUTES_RELATION).equals(qualifier)) {
						if (this.relationAttributes != null) {
							msg = "Duplicate relation attributes specification " + qualifier;
							throw new SpecificationParserException(msg);		
						}
						this.relationAttributes = parseAttributes(body, qualifier);
					} else if (qualifiers.get(QUALIFIER_ATTRIBUTES_BASE) != null && qualifiers.get(QUALIFIER_ATTRIBUTES_BASE).equals(qualifier)) {
						if (this.baseAttributes != null) {
							msg = "Duplicate base attributes specification " + qualifier;
							throw new SpecificationParserException(msg);
						}
						this.baseAttributes = parseAttributes(body, qualifier);
					}
				} else {
					msg = "Closing bracket of qualifier " + qualifier + " is missing";
					throw new SpecificationParserException(msg);
				}
				txt = txt.substring(++index).trim();
			} else if (!txt.isEmpty()){
				msg = "Invalid qualifier specified: " + txt;
				throw new SpecificationParserException(msg);
			}
		} while (body != null && qualifier != null);
		validateBaseAttributes();
		validateDependencies();
	}

	protected Set<FunctionalDependency> parseDependencies(String txt) throws SpecificationParserException {
		HashSet<FunctionalDependency> dependencySet = new HashSet<>();
		String [] tokens = txt.split(",");

		for (int i = 0; i < tokens.length; i++) {
			String token = tokens[i].trim();
			if (!token.isEmpty()) {
				FunctionalDependency dependency = parseDependency(token);
				if (dependencySet.contains(dependency)) {
					throw new SpecificationParserException("Duplicate dependencies specification " + dependency);
				}
				dependencySet.add(dependency);
			}
		}
		return dependencySet;
	}
		
	protected FunctionalDependency parseDependency(String txt) throws SpecificationParserException {
		String msg;
		FunctionalDependency dependency;
		String[] tokens;
		String token;
		String lhs;
		String rhs;
		
		lhs = null;
		rhs = null;
		dependency = new FunctionalDependency();
		tokens = txt.split("->");
		for (int j = 0; j < tokens.length; j++) {
			token = tokens[j].trim();
			if (lhs == null) {
				lhs = token.trim();
				dependency.setLHSAttributes(parseAttributes(lhs, (String)qualifiers.get(QUALIFIER_DEPENDENCIES)));
			} else if (rhs == null) {
				rhs = token.trim();
				dependency.setRHSAttributes(parseAttributes(rhs, (String)qualifiers.get(QUALIFIER_DEPENDENCIES)));
			} else {
				msg = "Invalid dependency definition: " + txt;
				throw new SpecificationParserException(msg);			
			}
		}
		if (lhs == null) {
			msg = "Missing left hand side of dependency " + txt;
			throw new SpecificationParserException(msg);			
		} else if (rhs == null) {
			msg = "Missing right hand side of dependency " + txt;
			throw new SpecificationParserException(msg);			
		}
		
		return dependency;
	}

	protected Set<String> parseAttributes(String txt, String qualifier) throws SpecificationParserException {
		Set<String> attributeSet = new TreeSet<>();
		String[] tokens = txt.split("\\s");

		for (int i = 0; i < tokens.length; i++) {
			String token = tokens[i].trim();
			if (!token.isEmpty()) {
				if (!token.matches(PATTERN_ATTRIBUTE)) {
					throw new SpecificationParserException("Invalid attribute: " + token);
				}
				if (attributeSet.contains(token)) {
					if (qualifier.equals(qualifiers.get(QUALIFIER_ATTRIBUTES_BASE))) {
						throw new SpecificationParserException("Duplicate attribute in base attributes specification " + qualifier);
					} else if (qualifier.equals(qualifiers.get(QUALIFIER_ATTRIBUTES_RELATION))) {
						throw new SpecificationParserException("Duplicate attribute in relation attributes specification " + qualifier);
					} else if (qualifier.equals(qualifiers.get(QUALIFIER_DEPENDENCIES))) {
						throw new SpecificationParserException("Duplicate attribute in dependency specification " + qualifier);
					}
				}
				attributeSet.add(token);
			}
		}
		return attributeSet;
	}

	protected void validateQualifier(String txt) throws SpecificationParserException {
		String msg;
		if (txt == null || txt.trim().isEmpty()) {
			msg = "No qualifier specified";
			throw new SpecificationParserException(msg);
		}
		if (!txt.equals(qualifiers.get(QUALIFIER_ATTRIBUTES_RELATION)) && !txt.equals(qualifiers.get(QUALIFIER_DEPENDENCIES)) && !txt.equals(qualifiers.get(QUALIFIER_ATTRIBUTES_BASE))) {
			msg = "Unexpected qualifier: " + txt;
			throw new SpecificationParserException(msg);
		}
	}
	
	protected void validateAttribute(String txt) throws SpecificationParserException {
		String msg;
		if (!txt.matches(PATTERN_ATTRIBUTE)) {
			msg = "Invalid attribute: " + txt;
			throw new SpecificationParserException(msg);
		}
	}
	
	protected void validateBaseAttributes() throws SpecificationParserException {
		if (this.baseAttributes == null) {
			return;
		}

		Set<String> set = new TreeSet<>(getUnfoundedAttributes(this.baseAttributes));
		
		if (!set.isEmpty()) {
			StringBuilder msg = new StringBuilder("Base attributes have been specified which are not part of the relation:");
			Iterator<String> it = set.iterator();
			while (it.hasNext()) {
				msg.append(" ").append(it.next());
			}
			throw new SpecificationParserException(msg.toString());
		}
	}

	protected void validateDependencies() throws SpecificationParserException {
		if (this.dependencies == null) {
			return;
		}

		Set<String> attributeSet = new TreeSet<>();
		Iterator it = this.dependencies.iterator();
		while (it.hasNext()) {
			FunctionalDependency dependency = (FunctionalDependency)it.next();
			attributeSet.addAll(getUnfoundedAttributes(dependency.getLHSAttributes()));
			attributeSet.addAll(getUnfoundedAttributes(dependency.getRHSAttributes()));
		}
		
		if (!attributeSet.isEmpty()) {
			StringBuilder msg = new StringBuilder("Attributes have been specified in dependencies which are not part of the relation:");
			it = attributeSet.iterator();
			while (it.hasNext()) {
				msg.append(" ").append(it.next());
			}
			throw new SpecificationParserException(msg.toString());
		}
	}
	
	protected Set<String> getUnfoundedAttributes(Collection<String> attributes) throws SpecificationParserException {
		Set<String> set = new TreeSet<>();
		Iterator<String> it = attributes.iterator();
		while (it.hasNext()) {
			String attribute = it.next();
			if (this.relationAttributes == null || !this.relationAttributes.contains(attribute)) {
				set.add(attribute);
			}
		}
		return set;
	}
	
	/**
	 * @return <code>null</code> if not specified, an empty set if no attributes are specified, 
	 * or a set containing all specified base attributes
	 */
	public Set<String> getBaseAttributes() {
		return baseAttributes;
	}

	/**
	 * @return <code>null</code> if not specified, an empty set if no dependencies are specified, 
	 * or a set containing all specified dependencies
	 */
	public Set<FunctionalDependency> getDependencies() {
		return dependencies;
	}

	/**
	 * @return <code>null</code> if not specified, an empty set if no relation attributes are specified, 
	 * or a set containing all specified relation attributes
	 */
	public Set<String> getRelationAttributes() {
		return relationAttributes;
	}
	
	private static void print(Collection items, String qualifier, String delim, StringBuffer buffer) {
		boolean first;

		buffer.append(qualifier).append(" {");
		first = true;
		Iterator it = items.iterator();
		while (it.hasNext()) {
			if (!first) {
				buffer.append(delim);
			} else {
				first = false;
			}
			buffer.append(it.next());
		}
		buffer.append("}");
	}

	public String getText(RDBDSpecification specification) {
		String msg;
		Relation relation;

		Vector<String> baseAttributes = null;
		StringBuilder buffer = new StringBuilder();
		
		//RDBD TYPE SPECIFIC TASKS
		if (specification instanceof Relation) {
			relation = (Relation)specification;
		} else if (specification instanceof AttributeClosureSpecification) {
			relation = ((AttributeClosureSpecification)specification).getBaseRelation();
			baseAttributes = ((AttributeClosureSpecification)specification).getBaseAttributes();
		} else if (specification instanceof RBRSpecification) {
			relation = ((RBRSpecification)specification).getBaseRelation();
			baseAttributes = ((RBRSpecification)specification).getBaseAttributes();
		} else if (specification instanceof DecomposeSpecification) {
			relation = ((DecomposeSpecification)specification).getBaseRelation();
		} else if (specification instanceof NormalizationSpecification) {
			relation = ((NormalizationSpecification)specification).getBaseRelation();
		} else {
			msg = "Passed serializable is not of any valid RDBD specification type: ";
			msg += specification == null ? "null" : specification.getClass().getName();
			throw new RuntimeException(msg);
		}
		
		if (relation != null && qualifiers.get(QUALIFIER_ATTRIBUTES_RELATION) != null) {
			print(relation.getAttributes(), (String)qualifiers.get(QUALIFIER_ATTRIBUTES_RELATION), " ", buffer);
			buffer.append(LINE_SEP);
		}
		if (relation != null && qualifiers.get(QUALIFIER_DEPENDENCIES) != null) {
			print(relation.getFunctionalDependencies(), (String)qualifiers.get(QUALIFIER_DEPENDENCIES), ", ", buffer);
			buffer.append(LINE_SEP);
		}
		if (baseAttributes != null && qualifiers.get(QUALIFIER_ATTRIBUTES_BASE) != null) {
			print(baseAttributes, (String)qualifiers.get(QUALIFIER_ATTRIBUTES_BASE), " ", buffer);
			buffer.append(LINE_SEP);
		}
		
		return buffer.toString();
	}
	
	public String getQualifier(String qualifierType) {
		return (String)qualifiers.get(qualifierType);
	}
	
	public static SpecificationParser createParser(int rdbdType) {
		switch (rdbdType) {
			case (RDBDConstants.TYPE_ATTRIBUTE_CLOSURE): {
				return new SpecificationParser("R", "A", "F");
			}
			case (RDBDConstants.TYPE_DECOMPOSE): {
				return new SpecificationParser("R", null, "F");
			}
			case (RDBDConstants.TYPE_KEYS_DETERMINATION): {
				return new SpecificationParser("R", null, "F");
			}
			case (RDBDConstants.TYPE_MINIMAL_COVER): {
				return new SpecificationParser(null, null, "F");
			}
			case (RDBDConstants.TYPE_NORMALFORM_DETERMINATION): {
				return new SpecificationParser("R", null, "F");
			}
			case (RDBDConstants.TYPE_NORMALIZATION): {
				return new SpecificationParser("R", null, "F");
			}
			case (RDBDConstants.TYPE_RBR): {
				return new SpecificationParser("R", "S", "F");
			}
			default: {
				return null;
			}
		}
	}

	public static void main(String[] args){
		/*
		SpecificationParser parser = new SpecificationParser();
		test(parser, "R {ab b cc} F {a -> c, a -> b} B {ab cc}");
		*/
		try {
			testKeysDetermination();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void test(SpecificationParser parser, String txt) {
		Iterator it;
		boolean first;
		try {
			parser.parse(txt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.print("RELATION ATTRIBUTES: ");
		if (parser.relationAttributes == null) {
			System.out.println("No specification.");
		} else if (parser.relationAttributes.isEmpty()) {
			System.out.println("Empty attribute list.");
		} else {
			first = true;
			it = parser.relationAttributes.iterator();
			while (it.hasNext()) {
				System.out.print((first ? "" : " " ) + it.next());
				first = false;
			}
			System.out.println();
		}
		System.out.print("BASE ATTRIBUTES: ");
		if (parser.baseAttributes == null) {
			System.out.println("No specification.");
		} else if (parser.baseAttributes.isEmpty()) {
			System.out.println("Empty attribute list.");
		} else {
			it = parser.baseAttributes.iterator();
			first = true;
			while (it.hasNext()) {
				System.out.print((first ? "" : " " ) + it.next());
				first = false;
			}
			System.out.println();
		}
		System.out.print("DEPENDENCIES: ");
		if (parser.dependencies == null) {
			System.out.println("No specification.");
		} else if (parser.dependencies.isEmpty()) {
			System.out.println("Empty dependencies list.");
		} else {
			it = parser.dependencies.iterator();
			first = true;
			while (it.hasNext()) {
				System.out.print((first ? "" : ", " ) + it.next());
				first = false;
			}
			System.out.println();
		}
	}
	
	private static void testNormalformDetermination() throws Exception {
		Iterator it;

		SpecificationParser parser = RDBDHelper.initParser(RDBDConstants.TYPE_NORMALFORM_DETERMINATION);
		parser.parse("R	{A B C D E }	F {A B -> C, C -> D, D E -> B, E -> C }");

		Relation specification = (Relation)RDBDHelper.initSpecification(RDBDConstants.TYPE_NORMALFORM_DETERMINATION);
		specification.setAttributes(parser.getRelationAttributes());
		specification.setFunctionalDependencies(parser.getDependencies());
		
		NormalformAnalyzerConfig normalformAnalyzerConfig = new NormalformAnalyzerConfig();
		normalformAnalyzerConfig.setCorrectMinimalKeys(KeysDeterminator.determineMinimalKeys((Relation)specification));
		normalformAnalyzerConfig.setRelation((Relation)specification);
		
		System.out.println("Minimal keys: ");
		it = normalformAnalyzerConfig.getCorrectMinimalKeys().iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}
		
		NormalformAnalysis analysis = NormalformAnalyzer.analyze(normalformAnalyzerConfig);
		NormalformLevel level = analysis.getOverallNormalformLevel();
		System.out.println("Overall level: " + level);
		System.out.println("Violations: ");
		System.out.println("\tFirst: ");
		it = analysis.getFirstNormalformViolations().iterator();
		while (it.hasNext()) {
			System.out.println(((NormalformViolation)it.next()).getFunctionalDependency());
		}
		System.out.println("\tSecond: ");
		it = analysis.getSecondNormalformViolations().iterator();
		while (it.hasNext()) {
			System.out.println(((NormalformViolation)it.next()).getFunctionalDependency());
		}
		System.out.println("\tThird: ");
		it = analysis.getThirdNormalformViolations().iterator();
		while (it.hasNext()) {
			System.out.println(((NormalformViolation)it.next()).getFunctionalDependency());
		}
		System.out.println("\tBCNF: ");
		it = analysis.getBoyceCoddNormalformViolations().iterator();
		while (it.hasNext()) {
			System.out.println(((NormalformViolation)it.next()).getFunctionalDependency());
		}
	}

	private static void testKeysDetermination() throws Exception {
		SpecificationParser parser = RDBDHelper.initParser(RDBDConstants.TYPE_KEYS_DETERMINATION);
		parser.parse("R{A B C D E} F{A B -> C, C -> D, D E -> B, E -> C }");

		Relation specification = (Relation)RDBDHelper.initSpecification(RDBDConstants.TYPE_KEYS_DETERMINATION);
		specification.setAttributes(parser.getRelationAttributes());
		specification.setFunctionalDependencies(parser.getDependencies());

		Collection<Key> keys = KeysDeterminator.determineMinimalKeys(specification);
		
		System.out.println("Minimal keys: ");
		Iterator<Key> it = keys.iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}
	}
	
	private static void testMinimalCover() throws Exception {
		HashSet correctDependencies;
		SpecificationParser parser;
		Relation specification;
		Iterator it;
		
		parser = RDBDHelper.initParser(RDBDConstants.TYPE_MINIMAL_COVER);
		parser.parse("R	{A B C D E }	F {A -> B C E, C E -> D, C D E -> B }");
		specification = (Relation)RDBDHelper.initSpecification(RDBDConstants.TYPE_MINIMAL_COVER);
		specification.setAttributes(parser.getRelationAttributes());
		specification.setFunctionalDependencies(parser.getDependencies());
		
		correctDependencies = MinimalCover.execute(specification.getFunctionalDependencies());
		
		System.out.println("\tCorrect dependencies: ");
		it = correctDependencies.iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}
	}
	
	private static void testAttributeClosure() throws Exception {
		SpecificationParser parser;
		AttributeClosureSpecification specification;
		Collection correctAttributes;
		Iterator it;
		
		parser = RDBDHelper.initParser(RDBDConstants.TYPE_ATTRIBUTE_CLOSURE);
		parser.parse("R	{A B C D E F G } F {A -> D G, A C -> E, C E -> D, D -> A, B C -> D, B E -> C, B C -> A } A	{A C}");
		specification = (AttributeClosureSpecification)RDBDHelper.initSpecification(RDBDConstants.TYPE_ATTRIBUTE_CLOSURE);
		specification.getBaseRelation().setAttributes(parser.getRelationAttributes());
		specification.getBaseRelation().setFunctionalDependencies(parser.getDependencies());
		specification.setBaseAttributes(parser.getBaseAttributes());
		
		correctAttributes = Closure.execute(specification.getBaseAttributes(), specification.getBaseRelation().getFunctionalDependencies());
		
		System.out.println("\tCorrect attributes: ");
		it = correctAttributes.iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}
	}
	
	private static void testRBR() throws Exception {
		SpecificationParser parser = RDBDHelper.initParser(RDBDConstants.TYPE_RBR);
		parser.parse("R	{A B C D E F  G H}F {A G -> B, E -> D, A B -> F, C D -> G, D -> B, A E H -> F, E F -> C, A E -> C }S{A D E F G H}");

		RBRSpecification specification = (RBRSpecification)RDBDHelper.initSpecification(RDBDConstants.TYPE_RBR);
		specification.getBaseRelation().setAttributes(parser.getRelationAttributes());
		specification.getBaseRelation().setFunctionalDependencies(parser.getDependencies());
		specification.setBaseAttributes(parser.getBaseAttributes());

		Collection<FunctionalDependency> correctDependencies = ReductionByResolution.execute(specification.getBaseRelation(), specification.getBaseAttributes());
		
		System.out.println("\tCorrect dependencies: ");
		Iterator<FunctionalDependency> it = correctDependencies.iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}
	}
}