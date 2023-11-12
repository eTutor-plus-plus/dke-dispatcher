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

	private Map qualifiers;
	private Set baseAttributes;
	private Set relationAttributes;
	private Set dependencies;
	
	private SpecificationParser() {
		this("R", "B", "F");
	}
	
	public SpecificationParser(String qualRelation, String qualBase, String qualDependencies) {
		super();
		reset();
		this.qualifiers = new HashMap();
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
		if (txt == null || txt.trim().length() < 1) {
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
			} else if (txt.length() > 0){
				msg = "Invalid qualifier specified: " + txt;
				throw new SpecificationParserException(msg);
			}
		} while (body != null && qualifier != null);
		validateBaseAttributes();
		validateDependencies();
	}

	protected Set parseDependencies(String txt) throws SpecificationParserException {
		String msg;
		String[] tokens;
		String token;
		HashSet set;
		FunctionalDependency dependency;

		set = new HashSet();
		tokens = txt.split(",");
		for (int i = 0; i < tokens.length; i++) {
			token = tokens[i].trim();
			if (token.length() > 0) {
				dependency = parseDependency(token);
				if (set.contains(dependency)) {
					msg = "Duplicate dependencies specification " + dependency;
					throw new SpecificationParserException(msg);
				}
				set.add(dependency);
			}
		}
		return set;
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

	protected Set parseAttributes(String txt, String qualifier) throws SpecificationParserException {
		String msg;
		String[] tokens;
		String token;
		Set set;
		
		set = new TreeSet();
		tokens = txt.split("\\s");
		for (int i = 0; i < tokens.length; i++) {
			token = tokens[i].trim();
			if (token.length() > 0) {
				if (!token.matches(PATTERN_ATTRIBUTE)) {
					msg = "Invalid attribute: " + token;
					throw new SpecificationParserException(msg);		
				}
				if (set.contains(token)) {
					if (qualifier.equals(qualifiers.get(QUALIFIER_ATTRIBUTES_BASE))) {
						msg = "Duplicate attribute in base attributes specification " + qualifier;
						throw new SpecificationParserException(msg);	
					} else if (qualifier.equals(qualifiers.get(QUALIFIER_ATTRIBUTES_RELATION))) {
						msg = "Duplicate attribute in relation attributes specification " + qualifier;
						throw new SpecificationParserException(msg);	
					} else if (qualifier.equals(qualifiers.get(QUALIFIER_DEPENDENCIES))) {
						msg = "Duplicate attribute in dependency specification " + qualifier;
						throw new SpecificationParserException(msg);	
					}
				}
				set.add(token);
			}
		}
		return set;
	}

	protected void validateQualifier(String txt) throws SpecificationParserException {
		String msg;
		if (txt == null || txt.trim().equals("")) {
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
		String msg;
		Iterator it;
		Set set;
		
		set = new TreeSet();
		set.addAll(getUnfoundedAttributes(this.baseAttributes));
		
		if (set.size() > 0) {
			msg = "Base attributes have been specified which are not part of the relation:";
			it = set.iterator();
			while (it.hasNext()) {
				msg += " " + it.next();
			}
			throw new SpecificationParserException(msg);
		}
	}

	protected void validateDependencies() throws SpecificationParserException {
		if (this.dependencies == null) {
			return;
		}
		String msg;
		Iterator it;
		Set set;
		FunctionalDependency dependency;
		
		set = new TreeSet();
		it = this.dependencies.iterator();
		while (it.hasNext()) {
			dependency = (FunctionalDependency)it.next();
			set.addAll(getUnfoundedAttributes(dependency.getLHSAttributes()));
			set.addAll(getUnfoundedAttributes(dependency.getRHSAttributes()));
		}
		
		if (set.size() > 0) {
			msg = "Attributes have been specified in dependencies which are not part of the relation:";
			it = set.iterator();
			while (it.hasNext()) {
				msg += " " + it.next();
			}
			throw new SpecificationParserException(msg);
		}
	}
	
	protected Set getUnfoundedAttributes(Collection attributes) throws SpecificationParserException {
		Object attribute;
		Iterator it;
		Set set;
		
		set = new TreeSet();
		it = attributes.iterator();
		while (it.hasNext()) {
			attribute = it.next();
			if (this.relationAttributes == null || !this.relationAttributes.contains(attribute)) {
				set.add(attribute);
			}
		}
		return set;
	}
	
	public String toString() {
		return super.toString();
	}
	
	/**
	 * @return <code>null</code> if not specified, an empty set if no attributes are specified, 
	 * or a set containing all specified base attributes
	 */
	public Set getBaseAttributes() {
		return baseAttributes;
	}

	/**
	 * @return <code>null</code> if not specified, an empty set if no dependencies are specified, 
	 * or a set containing all specified dependencies
	 */
	public Set getDependencies() {
		return dependencies;
	}

	/**
	 * @return <code>null</code> if not specified, an empty set if no relation attributes are specified, 
	 * or a set containing all specified relation attributes
	 */
	public Set getRelationAttributes() {
		return relationAttributes;
	}
	
	private static void print(Collection items, String qualifier, String delim, StringBuffer buffer) {
		Iterator it;
		boolean first;

		buffer.append(qualifier + " {");
		first = true;
		it = items.iterator();
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
		Vector baseAttributes;
		Relation relation;
		StringBuffer buffer;
		
		relation = null;
		baseAttributes = null;
		buffer = new StringBuffer();
		
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
		if (relation != null && (String)qualifiers.get(QUALIFIER_DEPENDENCIES) != null) {
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
		} else if (parser.relationAttributes.size() < 1) {
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
		} else if (parser.baseAttributes.size() < 1) {
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
		} else if (parser.dependencies.size() < 1) {
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
		NormalformAnalysis analysis;
		SpecificationParser parser;
		Relation specification;
		Iterator it;
		NormalformLevel level;
		
		parser = RDBDHelper.initParser(RDBDConstants.TYPE_NORMALFORM_DETERMINATION);
		parser.parse("R	{A B C D E }	F {A B -> C, C -> D, D E -> B, E -> C }");

		specification = (Relation)RDBDHelper.initSpecification(RDBDConstants.TYPE_NORMALFORM_DETERMINATION);
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
		
		analysis = NormalformAnalyzer.analyze(normalformAnalyzerConfig);
		level = analysis.getOverallNormalformLevel();
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
		it = analysis.getBoyceCottNormalformViolations().iterator();
		while (it.hasNext()) {
			System.out.println(((NormalformViolation)it.next()).getFunctionalDependency());
		}
	}

	private static void testKeysDetermination() throws Exception {
		SpecificationParser parser;
		Relation specification;
		Collection keys;
		Iterator it;
		
		parser = RDBDHelper.initParser(RDBDConstants.TYPE_KEYS_DETERMINATION);
		parser.parse("R{A B C D E} F{A B -> C, C -> D, D E -> B, E -> C }");

		specification = (Relation)RDBDHelper.initSpecification(RDBDConstants.TYPE_KEYS_DETERMINATION);
		specification.setAttributes(parser.getRelationAttributes());
		specification.setFunctionalDependencies(parser.getDependencies());
		
		keys = KeysDeterminator.determineMinimalKeys((Relation)specification);
		
		System.out.println("Minimal keys: ");
		it = keys.iterator();
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
		SpecificationParser parser;
		RBRSpecification specification;
		Collection correctDependencies;
		Iterator it;
		
		parser = RDBDHelper.initParser(RDBDConstants.TYPE_RBR);
		parser.parse("R	{A B C D E F  G H}F {A G -> B, E -> D, A B -> F, C D -> G, D -> B, A E H -> F, E F -> C, A E -> C }S{A D E F G H}");
		specification = (RBRSpecification)RDBDHelper.initSpecification(RDBDConstants.TYPE_RBR);
		specification.getBaseRelation().setAttributes(parser.getRelationAttributes());
		specification.getBaseRelation().setFunctionalDependencies(parser.getDependencies());
		specification.setBaseAttributes(parser.getBaseAttributes());

		correctDependencies = ReductionByResolution.execute(specification.getBaseRelation(), specification.getBaseAttributes());
		
		System.out.println("\tCorrect dependencies: ");
		it = correctDependencies.iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}
	}
}