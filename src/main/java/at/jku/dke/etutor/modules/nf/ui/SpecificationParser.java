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

	public static final String QUALIFIER_DEPENDENCIES = "1";
	public static final String QUALIFIER_ATTRIBUTES_BASE = "2";
	public static final String QUALIFIER_ATTRIBUTES_RELATION = "3";
	public static final String PATTERN_ATTRIBUTE = "\\s*\\w*\\s*";

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

        for (String s : tokens) {
            String token = s.trim();
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
        for (String s : tokens) {
            token = s.trim();
            if (lhs == null) {
                lhs = token.trim();
                dependency.setLhsAttributes(parseAttributes(lhs, qualifiers.get(QUALIFIER_DEPENDENCIES)));
            } else if (rhs == null) {
                rhs = token.trim();
                dependency.setRhsAttributes(parseAttributes(rhs, qualifiers.get(QUALIFIER_DEPENDENCIES)));
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

        for (String s : tokens) {
            String token = s.trim();
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
            for (String s : set) {
                msg.append(" ").append(s);
            }
			throw new SpecificationParserException(msg.toString());
		}
	}

	protected void validateDependencies() throws SpecificationParserException {
		if (this.dependencies == null) {
			return;
		}

		Set<String> attributeSet = new TreeSet<>();
        for (FunctionalDependency dependency : this.dependencies) {
            attributeSet.addAll(getUnfoundedAttributes(dependency.getLhsAttributes()));
            attributeSet.addAll(getUnfoundedAttributes(dependency.getRhsAttributes()));
        }
		
		if (!attributeSet.isEmpty()) {
			StringBuilder msg = new StringBuilder("Attributes have been specified in dependencies which are not part of the relation:");
            for (String s : attributeSet) {
                msg.append(" ").append(s);
            }
			throw new SpecificationParserException(msg.toString());
		}
	}
	
	protected Set<String> getUnfoundedAttributes(Collection<String> attributes) throws SpecificationParserException {
		Set<String> set = new TreeSet<>();
        for (String attribute : attributes) {
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
	
	private static void print(Collection<?> items, String qualifier, String delim, StringBuilder buffer) {
		boolean first;

		buffer.append(qualifier).append(" {");
		first = true;
        for (Object item : items) {
            if (!first) {
                buffer.append(delim);
            } else {
                first = false;
            }
            buffer.append(item);
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
			print(relation.getAttributes(), qualifiers.get(QUALIFIER_ATTRIBUTES_RELATION), " ", buffer);
			buffer.append(LINE_SEP);
		}
		if (relation != null && qualifiers.get(QUALIFIER_DEPENDENCIES) != null) {
			print(relation.getFunctionalDependencies(), qualifiers.get(QUALIFIER_DEPENDENCIES), ", ", buffer);
			buffer.append(LINE_SEP);
		}
		if (baseAttributes != null && qualifiers.get(QUALIFIER_ATTRIBUTES_BASE) != null) {
			print(baseAttributes, qualifiers.get(QUALIFIER_ATTRIBUTES_BASE), " ", buffer);
			buffer.append(LINE_SEP);
		}
		
		return buffer.toString();
	}
	
	public String getQualifier(String qualifierType) {
		return qualifiers.get(qualifierType);
	}
	
	public static SpecificationParser createParser(int rdbdType) {
        return switch (rdbdType) {
            case (RDBDConstants.TYPE_ATTRIBUTE_CLOSURE) -> new SpecificationParser("R", "A", "F");
            case (RDBDConstants.TYPE_DECOMPOSE) -> new SpecificationParser("R", null, "F");
            case (RDBDConstants.TYPE_KEYS_DETERMINATION) -> new SpecificationParser("R", null, "F");
            case (RDBDConstants.TYPE_MINIMAL_COVER) -> new SpecificationParser(null, null, "F");
            case (RDBDConstants.TYPE_NORMALFORM_DETERMINATION) -> new SpecificationParser("R", null, "F");
            case (RDBDConstants.TYPE_NORMALIZATION) -> new SpecificationParser("R", null, "F");
            case (RDBDConstants.TYPE_RBR) -> new SpecificationParser("R", "S", "F");
            default -> null;
        };
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
            for (String relationAttribute : parser.relationAttributes) {
                System.out.print((first ? "" : " ") + relationAttribute);
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
			Iterator<String> it = parser.baseAttributes.iterator();
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
			Iterator<FunctionalDependency> it = parser.dependencies.iterator();
			first = true;
			while (it.hasNext()) {
				System.out.print((first ? "" : ", " ) + it.next());
				first = false;
			}
			System.out.println();
		}
	}
	
	private static void testNormalformDetermination() throws Exception {
		SpecificationParser parser = RDBDHelper.initParser(RDBDConstants.TYPE_NORMALFORM_DETERMINATION);
		parser.parse("R	{A B C D E }	F {A B -> C, C -> D, D E -> B, E -> C }");

		Relation specification = (Relation)RDBDHelper.initSpecification(RDBDConstants.TYPE_NORMALFORM_DETERMINATION);
		specification.setAttributes(parser.getRelationAttributes());
		specification.setFunctionalDependencies(parser.getDependencies());
		
		NormalformAnalyzerConfig normalformAnalyzerConfig = new NormalformAnalyzerConfig();
		normalformAnalyzerConfig.setCorrectMinimalKeys(KeysDeterminator.determineMinimalKeys(specification));
		normalformAnalyzerConfig.setRelation(specification);
		
		System.out.println("Minimal keys: ");
        for (Key key : normalformAnalyzerConfig.getCorrectMinimalKeys()) {
            System.out.println(key);
        }
		
		NormalformAnalysis analysis = NormalformAnalyzer.analyze(normalformAnalyzerConfig);
		NormalformLevel level = analysis.getOverallNormalformLevel();
		System.out.println("Overall level: " + level);
		System.out.println("Violations: ");
		System.out.println("\tFirst: ");
		Iterator<? extends NormalformViolation> normalformViolationIt = analysis.getFirstNormalformViolations().iterator();
		while (normalformViolationIt.hasNext()) {
			System.out.println(normalformViolationIt.next().getFunctionalDependency());
		}
		System.out.println("\tSecond: ");
		normalformViolationIt = analysis.getSecondNormalformViolations().iterator();
		while (normalformViolationIt.hasNext()) {
			System.out.println(normalformViolationIt.next().getFunctionalDependency());
		}
		System.out.println("\tThird: ");
		normalformViolationIt = analysis.getThirdNormalformViolations().iterator();
		while (normalformViolationIt.hasNext()) {
			System.out.println(normalformViolationIt.next().getFunctionalDependency());
		}
		System.out.println("\tBCNF: ");
		normalformViolationIt = analysis.getBoyceCoddNormalformViolations().iterator();
		while (normalformViolationIt.hasNext()) {
			System.out.println(normalformViolationIt.next().getFunctionalDependency());
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
        for (Key key : keys) {
            System.out.println(key);
        }
	}
	
	private static void testMinimalCover() throws Exception {
		SpecificationParser parser = RDBDHelper.initParser(RDBDConstants.TYPE_MINIMAL_COVER);
		parser.parse("R	{A B C D E }	F {A -> B C E, C E -> D, C D E -> B }");
		Relation specification = (Relation)RDBDHelper.initSpecification(RDBDConstants.TYPE_MINIMAL_COVER);
		specification.setAttributes(parser.getRelationAttributes());
		specification.setFunctionalDependencies(parser.getDependencies());
		
		Set<FunctionalDependency> correctDependencies = MinimalCover.execute(specification.getFunctionalDependencies());
		
		System.out.println("\tCorrect dependencies: ");
        for (FunctionalDependency correctDependency : correctDependencies) {
            System.out.println(correctDependency);
        }
	}
	
	private static void testAttributeClosure() throws Exception {
		SpecificationParser parser;
		AttributeClosureSpecification specification;
		
		parser = RDBDHelper.initParser(RDBDConstants.TYPE_ATTRIBUTE_CLOSURE);
		parser.parse("R	{A B C D E F G } F {A -> D G, A C -> E, C E -> D, D -> A, B C -> D, B E -> C, B C -> A } A	{A C}");
		specification = (AttributeClosureSpecification)RDBDHelper.initSpecification(RDBDConstants.TYPE_ATTRIBUTE_CLOSURE);
		specification.getBaseRelation().setAttributes(parser.getRelationAttributes());
		specification.getBaseRelation().setFunctionalDependencies(parser.getDependencies());
		specification.setBaseAttributes(parser.getBaseAttributes());

		Collection<String> correctAttributes = Closure.execute(specification.getBaseAttributes(), specification.getBaseRelation().getFunctionalDependencies());
		
		System.out.println("\tCorrect attributes: ");
        for (String correctAttribute : correctAttributes) {
            System.out.println(correctAttribute);
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
        for (FunctionalDependency correctDependency : correctDependencies) {
            System.out.println(correctDependency);
        }
	}
}