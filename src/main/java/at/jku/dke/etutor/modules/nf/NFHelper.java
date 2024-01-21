package at.jku.dke.etutor.modules.nf;

import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;
import at.jku.dke.etutor.modules.nf.model.IdentifiedRelation;
import at.jku.dke.etutor.modules.nf.model.IdentifiedRelationComparator;
import at.jku.dke.etutor.modules.nf.model.Key;
import at.jku.dke.etutor.modules.nf.model.Relation;
import at.jku.dke.etutor.modules.nf.specification.AttributeClosureSpecification;
import at.jku.dke.etutor.modules.nf.specification.DecomposeSpecification;
import at.jku.dke.etutor.modules.nf.specification.NFSpecification;
import at.jku.dke.etutor.modules.nf.specification.NormalizationSpecification;
import at.jku.dke.etutor.modules.nf.specification.RBRSpecification;
import at.jku.dke.etutor.modules.nf.ui.HTMLPrinter;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Provides central resources for the RDBD module (loggers and database connections) 
 * and helper methods for dealing with all different kinds of {@link NFSpecification}
 * implementations within the RDBD module. This includes manipulation of specification objects, 
 * conversions between object and String representations of specifications, and helper methods 
 * aimed at common aspects of all specification implementations which are not part of the interface yet.
 * 
 * @author Georg Nitsche (10.01.2006)
 *
 */
public class NFHelper {

	private NFHelper() {
		// This class is not meant to be instantiated
	}
	
	private static int elementID;
	private static Logger rdbdLogger;
	private static final Logger testLogger;

	static {
		//INIT ELEMENT ID
		elementID = 0;
		
		//INIT TEST LOGGER
		testLogger = Logger.getLogger("RDBDTestLogger");

		//INIT RDBD LOGGER
		try {
			rdbdLogger = Logger.getLogger("etutor.modules.rdbd");
		} catch (Exception e){
			e.printStackTrace();		
		}
	}

	public static String getNextElementID(){
		elementID = elementID + 1;
		return "elementID" + elementID;
	}

	public static Logger getLogger(){
		return rdbdLogger;
	}
	
	public static Logger getTestLogger(){
		return testLogger;
	}


	public static synchronized Connection getPooledConnection() {
		Connection conn = null;
		
		try {
			/*
			 * NOTE: enough connections must be available if multiple connections are required within a single request
			 *  at the same time
			 */
			conn = NFDataSource.getConnection(); // Replaced with NFDataSource (Gerald Wimmer, 2023-11-30)
		} catch(SQLException e){
			rdbdLogger.log(Level.SEVERE, "", e);
		}
		
		return conn;
	}

	public static TreeSet<IdentifiedRelation> findSubRelations(String relationID, TreeSet<IdentifiedRelation> relations){
		IdentifiedRelation currRelation;
		Iterator<IdentifiedRelation> relationsIterator = relations.iterator();
		TreeSet<IdentifiedRelation> subRelations = new TreeSet<>(new IdentifiedRelationComparator());
		
		while (relationsIterator.hasNext()){
			currRelation = relationsIterator.next();
			NFHelper.getLogger().log(Level.INFO, "Check Relation: '" + currRelation.getID() + "'.");
			if ((currRelation.getID().startsWith(relationID)) && (currRelation.getID().length() > relationID.length())){
				subRelations.add(currRelation);
				NFHelper.getLogger().log(Level.INFO, "Found Sub Relation: '" + currRelation.getID() + "'.");
			}
		}
		
		return subRelations;
	}
	
	public static IdentifiedRelation findRelation(String relationID, TreeSet<IdentifiedRelation> relations){
		IdentifiedRelation relation;

        for (IdentifiedRelation identifiedRelation : relations) {
            relation = identifiedRelation;
            if (relation.getID().equals(relationID)) {
                return relation;
            }
        }
		
		return null;
	}

	/**
	 * Tests whether there is a subrelation for the specified relationID
	 * @param relationID The id of the relation which is to be tested for being an inner node
	 * @param relations The <code>Collection</code> of relations within which the relationID is supposed to be located
	 * @return Whether there is a subrelation for the specified relationID
	 */
	public static boolean isInnerNode(String relationID, Collection<IdentifiedRelation> relations){
		return relations.stream()
				.anyMatch(identifiedRelation -> identifiedRelation.getID().startsWith(relationID) && identifiedRelation.getID().length() > relationID.length());
	}

	public static void addFunctionalDependency(String lhsAttributes, String rhsAttributes, Relation relation){
		String[] temp;
		FunctionalDependency dependency = new FunctionalDependency();
		
		temp = lhsAttributes.split(" ");
        for (String string : temp) {
            dependency.addLHSAttribute(string);
        }
	
		temp = rhsAttributes.split(" ");
        for (String s : temp) {
            dependency.addRHSAttribute(s);
        }
		
		relation.addFunctionalDependency(dependency);
	}

	public static void newRelation(Collection<IdentifiedRelation> relations) {
		int maxID;
		StringBuilder increasedID;
		IdentifiedRelation newRelation;
		IdentifiedRelation currRelation;

		maxID = 0;
		newRelation = new IdentifiedRelation();
        for (IdentifiedRelation relation : relations) {
            currRelation = relation;
            if (Integer.parseInt(currRelation.getID().replaceAll("\\.", "")) > maxID) {
                maxID = Integer.parseInt(currRelation.getID().replaceAll("\\.", ""));
            }
        }
		
		increasedID = new StringBuilder(String.valueOf(maxID + 1));
		
		for (int i=0; i<increasedID.length(); i++){
			if ((increasedID.charAt(i) != '.') && (i+1 <increasedID.length())){
				increasedID.insert(i+1, ".");
			}
		}
		
		newRelation.setID(increasedID.toString());
		relations.add(newRelation);
	}

	public static void delRelation(Collection<IdentifiedRelation> relations, String relationID) {
		int currID;
		boolean found;
		StringBuilder reducedID;
		IdentifiedRelation currRelation;

		if ((relationID != null) && (!relationID.isEmpty()) && (relations != null)){

			found = false;
			Iterator<IdentifiedRelation> relationsIterator = relations.iterator();

			while (relationsIterator.hasNext()){
				currRelation = relationsIterator.next();

				if (currRelation.getID().equals(relationID)){
					relationsIterator.remove();
					found = true;
				}
				if (found){
					currID = Integer.parseInt(currRelation.getID().replaceAll("\\.", ""));
					reducedID = new StringBuilder(String.valueOf(currID-1));

					for (int i=0; i<reducedID.length(); i++){
						if ((reducedID.charAt(i) != '.') && (i+1 <reducedID.length())){
							reducedID.insert(i+1, ".");
						}
					}
					
					currRelation.setID(reducedID.toString());
				}
			}
		}
	}
	
	public static void addAttribute(Collection<IdentifiedRelation> relations, String relationID, String[] attributes) throws Exception{
		IdentifiedRelation currRelation;

		if ((relationID != null) && 
				(!relationID.isEmpty()) &&
				(attributes != null) && 
				(attributes.length != 0)){

            for (IdentifiedRelation relation : relations) {
                currRelation = relation;
                if (currRelation.getID().equals(relationID)) {
                    addAttribute(currRelation, attributes);
                }
            }
		}
	}
	
	public static void addAttribute(Relation relation, String[] attributes) {
        for (String attribute : attributes) {
            relation.addAttribute(attribute);
        }
	}
	
	public static void delAttributes(Collection<IdentifiedRelation> relations, String relationID, String[] attributesToDelete) throws Exception{
		IdentifiedRelation currRelation;

		if ((relationID != null) && 
				(!relationID.isEmpty()) &&
				(attributesToDelete != null) && 
				(attributesToDelete.length != 0)){

            for (IdentifiedRelation relation : relations) {
                currRelation = relation;
                if (currRelation.getID().equals(relationID)) {
                    delAttributes(currRelation, attributesToDelete);
                }
            }
		}

		checkRelations(relations);
	}

	public static void delAttributes(Relation relation, String[] attributesToDelete) {
		String currAttribute;
        for (String s : attributesToDelete) {
            currAttribute = s;
            relation.removeAttribute(currAttribute);
        }
		checkRelation(relation);
	}
	
	public static void addDependency(Collection<IdentifiedRelation> relations, String relationID, String[] rhsAttributes, String[] lhsAttributes) throws Exception{
		IdentifiedRelation currRelation;

		if ((relationID != null) && 
			(!relationID.isEmpty()) &&
			(rhsAttributes != null) &&
			(rhsAttributes.length != 0) &&
			(lhsAttributes != null) &&
			(lhsAttributes.length != 0)){

            for (IdentifiedRelation relation : relations) {
                currRelation = relation;
                if (currRelation.getID().equals(relationID)) {
                    addDependency(currRelation, rhsAttributes, lhsAttributes);
                }
            }
		}
	}
	
	public static void addDependency(Relation relation, String[] rhsAttributes, String[] lhsAttributes) {
		FunctionalDependency currDependency;

		currDependency = new FunctionalDependency();

        for (String rhsAttribute : rhsAttributes) {
            currDependency.addRHSAttribute(rhsAttribute);
        }

        for (String lhsAttribute : lhsAttributes) {
            currDependency.addLHSAttribute(lhsAttribute);
        }
				
		relation.addFunctionalDependency(currDependency);
	}
	
	public static void delDependencies(Collection<IdentifiedRelation> relations, String relationID, String[] rhsAttributes, String[] lhsAttributes, String[] dependenciesToDelete) throws Exception{
		IdentifiedRelation currRelation;

		if ((relationID != null) && (!relationID.isEmpty())){

            for (IdentifiedRelation relation : relations) {
                currRelation = relation;
                if (currRelation.getID().equals(relationID)) {
                    delDependencies(currRelation, dependenciesToDelete);
                }
            }
		}
	}
	
	public static void delDependencies(Relation relation, String[] dependenciesToDelete) {
		String currDependency;
		FunctionalDependency dependencyToDelete;

        for (String s : dependenciesToDelete) {
            currDependency = s;
            dependencyToDelete = new FunctionalDependency();

			String[] lhsAttributes = currDependency.substring(0, currDependency.indexOf("->")).split(" ");
			String[] rhsAttributes = currDependency.substring(currDependency.indexOf("->") + 3).split(" ");

            for (String lhsAttribute : lhsAttributes) {
                dependencyToDelete.addLHSAttribute(lhsAttribute);
            }

            for (String rhsAttribute : rhsAttributes) {
                dependencyToDelete.addRHSAttribute(rhsAttribute);
            }

            relation.removeFunctionalDependency(dependencyToDelete);
        }
	}

	public static void addKey(Collection<IdentifiedRelation> relations, String relationID, String[] attributes) throws Exception{
		IdentifiedRelation currRelation;

		if ((relationID != null) && 
			(!relationID.isEmpty()) &&
			(attributes != null) &&
			(attributes.length != 0)){

            for (IdentifiedRelation relation : relations) {
                currRelation = relation;
                if (currRelation.getID().equals(relationID)) {
                    addKey(currRelation, attributes);
                }
            }
		}
	}
	
	public static void addKey(Relation relation, String[] attributes) {
		Key currKey;

		currKey = new Key();

        for (String attribute : attributes) {
            currKey.addAttribute(attribute);
        }

		relation.addMinimalKey(currKey);
	}

	public static void delKeys(Collection<IdentifiedRelation> relations, String relationID, String[] attributes, String[] keysToDelete) throws Exception{
		IdentifiedRelation currRelation;

		NFHelper.getLogger().log(Level.INFO, "DELETING KEYS");

		if ((relationID != null) && (!relationID.isEmpty())){
			NFHelper.getLogger().log(Level.INFO, "SEARCHING FOR RELATION '" + relationID + "'");

            for (IdentifiedRelation relation : relations) {
                currRelation = relation;
                if (currRelation.getID().equals(relationID)) {
                    NFHelper.getLogger().log(Level.INFO, "FOUND RELATION '" + relationID + "'");
                    delKeys(currRelation, keysToDelete);
                }
            }
		}
	}
	
	public static void delKeys(Relation relation, String[] keysToDelete) {
		Key keyToDelete;

        for (String s : keysToDelete) {
            NFHelper.getLogger().log(Level.INFO, "DELETING NEXT KEY");

            keyToDelete = new Key();
			String[] attributes = s.split(" ");
            for (String attribute : attributes) {
                keyToDelete.addAttribute(attribute);
            }

            boolean removed = relation.removeMinimalKey(keyToDelete);
            if (removed) {
                NFHelper.getLogger().log(Level.INFO, "REMOVED KEY '" + keyToDelete + "'.");
            } else {
                NFHelper.getLogger().log(Level.INFO, "COULD NOT REMOVE KEY '" + keyToDelete + "'.");
            }
        }
	}
	
	public static void checkRelations(Collection<? extends Relation> relations) {
		Relation relation;

        for (Relation value : relations) {
            relation = value;
            checkRelation(relation);
        }
	}
	
	public static void checkRelation(Relation relation) {
		Key key;
		FunctionalDependency dependency;
		
		Collection<String> attributes = relation.getAttributes();

		Iterator<FunctionalDependency> dependenciesIterator = relation.iterFunctionalDependencies();
		while (dependenciesIterator.hasNext()){
			dependency = dependenciesIterator.next();
			if ((!attributes.containsAll(dependency.getLhsAttributes())) || (!attributes.containsAll(dependency.getRhsAttributes()))){
				dependenciesIterator.remove();
			}
		}

		Iterator<Key> keysIterator = relation.iterMinimalKeys();
		while (keysIterator.hasNext()){
			key = keysIterator.next();
			if (!attributes.containsAll(key.getAttributes())){			
				keysIterator.remove();
			}
		}
	}

	public static String getAssignmentText(NFSpecification specTmp, int indent, Locale locale, NFConstants.Type rdbdType) {
		return switch (rdbdType) {
			case NORMALFORM_DETERMINATION -> HTMLPrinter.printAssignmentForNormalformDetermination(specTmp.getBaseRelation(), indent, locale);
			case KEYS_DETERMINATION -> HTMLPrinter.printAssignmentForKeysDetermination(specTmp.getBaseRelation(), indent, locale);
			case MINIMAL_COVER -> HTMLPrinter.printAssignmentForMinimalCover(specTmp.getBaseRelation(), indent, locale);
			case ATTRIBUTE_CLOSURE -> HTMLPrinter.printAssignmentForAttributeClosure((AttributeClosureSpecification)specTmp, indent, locale);
			case RBR -> HTMLPrinter.printAssignmentForRBR((RBRSpecification)specTmp, indent, locale);
			case NORMALIZATION -> HTMLPrinter.printAssignmentForNormalization((NormalizationSpecification)specTmp, indent, locale);
			case DECOMPOSE -> HTMLPrinter.printAssignmentForDecompose((DecomposeSpecification)specTmp, indent, locale);
			default -> throw new RuntimeException("Unexpected RDBD type: " + rdbdType);
		};
	}
}