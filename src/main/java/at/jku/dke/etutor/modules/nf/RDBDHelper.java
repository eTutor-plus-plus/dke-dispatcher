package at.jku.dke.etutor.modules.nf;

import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;
import at.jku.dke.etutor.modules.nf.model.Key;
import at.jku.dke.etutor.modules.nf.model.NormalformLevel;
import at.jku.dke.etutor.modules.nf.model.Relation;
import at.jku.dke.etutor.modules.nf.ui.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.Id;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Provides central resources for the RDBD module (loggers and database connections) 
 * and helper methods for dealing with all different kinds of {@link RDBDSpecification}
 * implementations within the RDBD module. This includes manipulation of specification objects, 
 * conversions between object and String representations of specifications, and helper methods 
 * aimed at common aspects of all specification implementations which are not part of the interface yet.
 * 
 * @author Georg Nitsche (10.01.2006)
 *
 */
public class RDBDHelper {
	
	private static int elementID;
	private static Logger rdbdLogger;
	private static final Logger testLogger;
	private static final Connection permanentConn;
	private static DataSource dataSource;

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

		//INIT PERMANENT CONNECTION TO RDBD DATABASE
		permanentConn = getNewConnection(); 
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

	public static Connection getPermanentConnection(){
		return permanentConn;
	}

	public static Connection getNewConnection(){
		Connection conn = null;
		
		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			conn = DriverManager.getConnection(RDBDConstants.CONN_STRING, RDBDConstants.CONN_USER, RDBDConstants.CONN_PWD);
		} catch(SQLException e){
			rdbdLogger.log(Level.SEVERE, "", e);
		}
		
		return conn;
	}

	
	public static synchronized Connection getPooledConnection() {
		Connection conn = null;
		
		try {
			//NOTE: enough connections must be available if multiple 
			//connections are required within a single request at the same time
			conn = getDataSource().getConnection();
		} catch(SQLException e){
			rdbdLogger.log(Level.SEVERE, "", e);
		}
		
		return conn;
	}

	// TODO: Replace with HikariDataSource like in SQL module (Gerald Wimmer, 2023-11-12)
	private static synchronized DataSource getDataSource() {
		if (dataSource == null) {
			try {
				InitialContext ctx = new InitialContext();
				Hashtable<?, ?> env = ctx.getEnvironment();
				
				String msg = "Main environment properties effectively set for JNDI context: \n";
				msg += Context.INITIAL_CONTEXT_FACTORY + "=";
				msg += env.get(Context.INITIAL_CONTEXT_FACTORY) + "\n";
				msg += Context.PROVIDER_URL + "=";
				msg += env.get(Context.PROVIDER_URL) + "\n";
				rdbdLogger.log(Level.INFO, msg);
	
				msg = "Trying to lookup " + DataSource.class.getName() + " object at " + RDBDConstants.NAMING_DATASOURCE;
				rdbdLogger.log(Level.INFO, msg);
	
				dataSource = (DataSource)ctx.lookup(RDBDConstants.NAMING_DATASOURCE);
			} catch (NamingException e) {
				String msg = "Exception when trying to retrieve a " + DataSource.class.getName()
				+ " object from JNDI context, mapped to the context '" 
				+ RDBDConstants.NAMING_DATASOURCE + "'. ";
		        rdbdLogger.log(Level.SEVERE, msg, e);
		    }
		}

		return dataSource;
	}
	
	public static TreeSet<IdentifiedRelation> findSubRelations(String relationID, TreeSet<IdentifiedRelation> relations){
		IdentifiedRelation currRelation;
		Iterator<IdentifiedRelation> relationsIterator = relations.iterator();
		TreeSet<IdentifiedRelation> subRelations = new TreeSet<IdentifiedRelation>(new IdentifiedRelationComparator());
		
		while (relationsIterator.hasNext()){
			currRelation = relationsIterator.next();
			RDBDHelper.getLogger().log(Level.INFO, "Check Relation: '" + currRelation.getID() + "'.");			
			if ((currRelation.getID().startsWith(relationID)) && (currRelation.getID().length() > relationID.length())){
				subRelations.add(currRelation);
				RDBDHelper.getLogger().log(Level.INFO, "Found Sub Relation: '" + currRelation.getID() + "'.");			
			}
		}
		
		return subRelations;
	}
	
	public static IdentifiedRelation findRelation(String relationID, TreeSet<IdentifiedRelation> relations){
		IdentifiedRelation relation;

		Iterator<IdentifiedRelation> i = relations.iterator();
		while (i.hasNext()){
			relation = i.next();
			if (relation.getID().equals(relationID)){
				return relation;
			}
		}
		
		return null;
	}
	
	public static boolean isInnerNode(String relationID, Collection<IdentifiedRelation> relations){
		String currID;
		boolean isInnerNode = false;
		IdentifiedRelation currRelation;
		Iterator<IdentifiedRelation> it = relations.iterator();
	
		while ((it.hasNext()) && (!isInnerNode)){
			
			currRelation = it.next();
			//RDBDHelper.getLogger().log(Level.INFO, "RELATION IS NULL: " + (currRelation == null));
			
			currID = currRelation.getID();
			//RDBDHelper.getLogger().log(Level.INFO, "ID IS NULL: " + (currID == null));

			if (currID.startsWith(relationID) && currID.length() > relationID.length()){
				isInnerNode = true;
			}
		}
	
		return isInnerNode;
	}

	public static Relation createRelation(String name, String attributes) throws Exception{
		Relation relation = new Relation();
		relation.setName(name);
		
		String[] temp = attributes.split(" ");
		for (int i=0; i<temp.length; i++){
			relation.addAttribute(temp[i]);
		}
		
		return relation; 
	}

	public static IdentifiedRelation createIdentifiedRelation(String id, String name, String attributes) throws Exception{
		IdentifiedRelation relation = new IdentifiedRelation();
		relation.setID(id);
		relation.setName(name);
		
		String[] temp = attributes.split(" ");
		for (int i=0; i<temp.length; i++){
			relation.addAttribute(temp[i]);
		}
		
		return relation; 
	}

	public static void addFunctionalDependency(String lhsAttributes, String rhsAttributes, Relation relation){
		String[] temp;
		FunctionalDependency dependency = new FunctionalDependency();
		
		temp = lhsAttributes.split(" ");
		for (int i=0; i<temp.length; i++){
			dependency.addLHSAttribute(temp[i]);
		}
	
		temp = rhsAttributes.split(" ");
		for (int i=0; i<temp.length; i++){
			dependency.addRHSAttribute(temp[i]);
		}
		
		relation.addFunctionalDependency(dependency);
	}
	
	public static void splitRelation(Collection<IdentifiedRelation> relations, String relationID, String[] subRelation1Attributes, String[] subRelation2Attributes) throws Exception{
		IdentifiedRelation subRelation1 = new IdentifiedRelation();
		IdentifiedRelation subRelation2 = new IdentifiedRelation();

		subRelation1.setID(relationID + ".1");
		subRelation1.setName("R" + relationID + ".1");
		for (int i=0; i<subRelation1Attributes.length; i++){
			subRelation1.addAttribute(subRelation1Attributes[i]);
		}

		subRelation2.setID(relationID + ".2");
		subRelation2.setName("R" + relationID + ".2");
		for (int i=0; i<subRelation2Attributes.length; i++){
			subRelation2.addAttribute(subRelation2Attributes[i]);
		}

		relations.add(subRelation1);
		relations.add(subRelation2);
	}
	
	public static void delSubRelations(Collection<IdentifiedRelation> relations, String relationID) throws Exception{
		String currRelationID;
		
		Iterator<IdentifiedRelation> relationsIterator = relations.iterator();

		while (relationsIterator.hasNext()){
			currRelationID = relationsIterator.next().getID();

			if (currRelationID.startsWith(relationID) && (currRelationID.length() > relationID.length())){
				relationsIterator.remove();
			}
		}
	}
	
	public static void newRelation(Collection<IdentifiedRelation> relations) throws Exception{
		int maxID;
		StringBuilder increasedID;
		IdentifiedRelation newRelation;
		IdentifiedRelation currRelation;

		maxID = 0;
		newRelation = new IdentifiedRelation();
		Iterator<IdentifiedRelation> relationsIterator = relations.iterator();
		while (relationsIterator.hasNext()){
			currRelation = relationsIterator.next();
			if (Integer.parseInt(currRelation.getID().replaceAll("\\.", ""))>maxID){
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

	public static void delRelation(Collection<IdentifiedRelation> relations, String relationID) throws Exception{
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

			Iterator<IdentifiedRelation> relationsIterator = relations.iterator();
			while (relationsIterator.hasNext()){
				currRelation = relationsIterator.next();
				if (currRelation.getID().equals(relationID)){
					addAttribute(currRelation, attributes);
				}
			}
		}
	}
	
	public static void addAttribute(Relation relation, String[] attributes) throws Exception{
		for (int i=0; i<attributes.length; i++){
			relation.addAttribute(attributes[i]);
		}
	}
	
	public static void delAttributes(Collection<IdentifiedRelation> relations, String relationID, String[] attributesToDelete) throws Exception{
		IdentifiedRelation currRelation;

		if ((relationID != null) && 
				(!relationID.isEmpty()) &&
				(attributesToDelete != null) && 
				(attributesToDelete.length != 0)){

			Iterator<IdentifiedRelation> relationsIterator = relations.iterator();
			while (relationsIterator.hasNext()){
				currRelation = relationsIterator.next();
				if (currRelation.getID().equals(relationID)){
					delAttributes(currRelation, attributesToDelete);
				}
			}
		}

		checkRelations(relations);
	}

	public static void delAttributes(Relation relation, String[] attributesToDelete) throws Exception{
		String currAttribute;
		for (int i=0; i<attributesToDelete.length; i++){
			currAttribute = attributesToDelete[i];
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

			Iterator<IdentifiedRelation> relationsIterator = relations.iterator();
			while (relationsIterator.hasNext()){
				currRelation = relationsIterator.next();
				if (currRelation.getID().equals(relationID)){
					addDependency(currRelation, rhsAttributes, lhsAttributes);
				}
			}
		}
	}
	
	public static void addDependency(Relation relation, String[] rhsAttributes, String[] lhsAttributes) throws Exception{
		FunctionalDependency currDependency;

		currDependency = new FunctionalDependency();

		for (int i=0; i<rhsAttributes.length; i++){
			currDependency.addRHSAttribute(rhsAttributes[i]);
		}

		for (int i=0; i<lhsAttributes.length; i++){
			currDependency.addLHSAttribute(lhsAttributes[i]);
		}
				
		relation.addFunctionalDependency(currDependency);
	}
	
	public static void delDependencies(Collection<IdentifiedRelation> relations, String relationID, String[] rhsAttributes, String[] lhsAttributes, String[] dependenciesToDelete) throws Exception{
		IdentifiedRelation currRelation;

		if ((relationID != null) && (!relationID.isEmpty())){

			Iterator<IdentifiedRelation> relationsIterator = relations.iterator();
			while (relationsIterator.hasNext()){
				currRelation = relationsIterator.next();
				if (currRelation.getID().equals(relationID)){
					delDependencies(currRelation, rhsAttributes, lhsAttributes, dependenciesToDelete);
				}
			}
		}
	}
	
	public static void delDependencies(Relation relation, String[] rhsAttributes, String[] lhsAttributes, String[] dependenciesToDelete) throws Exception{
		String currDependency;
		FunctionalDependency dependencyToDelete;

		for (int i=0; i<dependenciesToDelete.length; i++){
			currDependency = dependenciesToDelete[i];
			dependencyToDelete = new FunctionalDependency();
	
			lhsAttributes = currDependency.substring(0, currDependency.indexOf("->")).split(" ");					
			rhsAttributes = currDependency.substring(currDependency.indexOf("->") + 3).split(" ");
			
			for (int j=0; j<lhsAttributes.length; j++){
				dependencyToDelete.addLHSAttribute(lhsAttributes[j]);
			}
	
			for (int j=0; j<rhsAttributes.length; j++){
				dependencyToDelete.addRHSAttribute(rhsAttributes[j]);
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

			Iterator<IdentifiedRelation> relationsIterator = relations.iterator();
			while (relationsIterator.hasNext()){
				currRelation = relationsIterator.next();
				if (currRelation.getID().equals(relationID)){
					addKey(currRelation, attributes);
				}
			}
		}
	}
	
	public static void addKey(Relation relation, String[] attributes) throws Exception{
		Key currKey;

		currKey = new Key();

		for (int i=0; i<attributes.length; i++){
			currKey.addAttribute(attributes[i]);
		}

		relation.addMinimalKey(currKey);
	}

	public static void delKeys(Collection<IdentifiedRelation> relations, String relationID, String[] attributes, String[] keysToDelete) throws Exception{
		IdentifiedRelation currRelation;

		RDBDHelper.getLogger().log(Level.INFO, "DELETING KEYS");

		if ((relationID != null) && (!relationID.isEmpty())){
			RDBDHelper.getLogger().log(Level.INFO, "SEARCHING FOR RELATION '" + relationID + "'");

			Iterator<IdentifiedRelation> relationsIterator = relations.iterator();
			while (relationsIterator.hasNext()){
				currRelation = relationsIterator.next();
				if (currRelation.getID().equals(relationID)){
					RDBDHelper.getLogger().log(Level.INFO, "FOUND RELATION '" + relationID + "'");
					delKeys(currRelation, attributes, keysToDelete);
				}
			}
		}
	}
	
	public static void delKeys(Relation relation, String[] attributes, String[] keysToDelete) throws Exception {
		Key keyToDelete;

		for (int i=0; i<keysToDelete.length; i++){
			RDBDHelper.getLogger().log(Level.INFO, "DELETING NEXT KEY");

			keyToDelete = new Key();
			attributes = keysToDelete[i].split(" ");					
			for (int j=0; j<attributes.length; j++){
				keyToDelete.addAttribute(attributes[j]);
			}

			boolean removed = relation.removeMinimalKey(keyToDelete);
			if (removed){
				RDBDHelper.getLogger().log(Level.INFO, "REMOVED KEY '" + keyToDelete + "'.");						
			} else {
				RDBDHelper.getLogger().log(Level.INFO, "COULD NOT REMOVE KEY '" + keyToDelete + "'.");						
			}
		}
	}
	
	public static void checkRelations(Collection<Relation> relations) {
		Relation relation;

		Iterator<Relation> relationsIterator = relations.iterator();
		while (relationsIterator.hasNext()){
			relation = relationsIterator.next();
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
			if ((!attributes.containsAll(dependency.getLHSAttributes())) || (!attributes.containsAll(dependency.getRHSAttributes()))){			
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
	
	public static void addBaseAttributes(Serializable spec, String[] attributes) {
		if (attributes == null || attributes.length < 1) {
			return;
		}
		if (spec instanceof AttributeClosureSpecification) {
			for (int i = 0; i < attributes.length; i++) {
				((AttributeClosureSpecification)spec).addBaseAttribute(attributes[i]);
			}
		} else if (spec instanceof RBRSpecification) {
			for (int i = 0; i < attributes.length; i++) {
				((RBRSpecification)spec).addBaseAttribute(attributes[i]);
			}
		}
	}
	
	public static void setBaseAttributes(Serializable spec, Collection<String> attributes) {
		if (attributes == null) {
			return;
		}
		if (spec instanceof AttributeClosureSpecification) {
			((AttributeClosureSpecification)spec).setBaseAttributes(attributes);
		} else if (spec instanceof RBRSpecification) {
			((RBRSpecification)spec).setBaseAttributes(attributes);
		}
	}
	
	public static void delBaseAttributes(Serializable spec, String[] attributesToDelete) {
		Vector<String> attributes;
		boolean remove;
		if (attributesToDelete == null || attributesToDelete.length < 1) {
			return;
		}
		if (spec instanceof AttributeClosureSpecification) {
			attributes = ((AttributeClosureSpecification)spec).getBaseAttributes();
			((AttributeClosureSpecification)spec).setBaseAttributes(new Vector<>());
			for (int i = 0; i < attributes.size(); i++) {
				remove = false;
				for (int j = 0; !remove && j < attributesToDelete.length; j++) {
					if (attributesToDelete[j].equals(attributes.get(i))) {
						remove = true;
					}
				}
				if (!remove) {
					((AttributeClosureSpecification)spec).addBaseAttribute(attributes.get(i));
				}
			}
		} else if (spec instanceof RBRSpecification) {
			attributes = ((RBRSpecification)spec).getBaseAttributes();
			((RBRSpecification)spec).setBaseAttributes(new Vector<>());
			for (int i = 0; i < attributes.size(); i++) {
				remove = false;
				for (int j = 0; !remove && j < attributesToDelete.length; j++) {
					if (attributesToDelete[j].equals(attributes.get(i))) {
						remove = true;
					}
				}
				if (!remove) {
					((RBRSpecification)spec).addBaseAttribute(attributes.get(i));
				}
			}
		}
	}
	
	public static Relation getRelation(RDBDSpecification spec, int rdbdType) {
		switch (rdbdType) {
			case (RDBDConstants.TYPE_NORMALFORM_DETERMINATION): {
				return (Relation)spec;
			}
			case (RDBDConstants.TYPE_KEYS_DETERMINATION): {
				return (IdentifiedRelation)spec;
			}
			case (RDBDConstants.TYPE_MINIMAL_COVER): {
				return (IdentifiedRelation)spec;
			}
			case (RDBDConstants.TYPE_ATTRIBUTE_CLOSURE): {
				return ((AttributeClosureSpecification)spec).getBaseRelation();
			}
			case (RDBDConstants.TYPE_RBR): {
				return ((RBRSpecification)spec).getBaseRelation();
			}
			case (RDBDConstants.TYPE_NORMALIZATION): {
				return ((NormalizationSpecification)spec).getBaseRelation();
			}
			case (RDBDConstants.TYPE_DECOMPOSE): {
				return ((DecomposeSpecification)spec).getBaseRelation();
			}
			default: {
				throw new RuntimeException("Unexpected RDBD type: " + rdbdType);
			}
		}
	}

	public static RDBDSpecification clone(RDBDSpecification specToClone, int rdbdType) throws MalformedRelationIDException {
		//TODO: add clone() to interface RDBDSpecification (compatible with serialized specifications?)
		try {
			switch (rdbdType) {
				case (RDBDConstants.TYPE_NORMALFORM_DETERMINATION): {
					return (Relation)((Relation)specToClone).clone();
				}
				case (RDBDConstants.TYPE_KEYS_DETERMINATION): {
					return (IdentifiedRelation)((IdentifiedRelation)specToClone).clone();
				}
				case (RDBDConstants.TYPE_MINIMAL_COVER): {
					return (IdentifiedRelation)((IdentifiedRelation)specToClone).clone();
				}
				case (RDBDConstants.TYPE_ATTRIBUTE_CLOSURE): {
					return (AttributeClosureSpecification)((AttributeClosureSpecification)specToClone).clone();
				}
				case (RDBDConstants.TYPE_RBR): {
					return (RBRSpecification)((RBRSpecification)specToClone).clone();
				}
				case (RDBDConstants.TYPE_NORMALIZATION): {
					return (NormalizationSpecification)((NormalizationSpecification)specToClone).clone();
				}
				case (RDBDConstants.TYPE_DECOMPOSE): {
					return (DecomposeSpecification)((DecomposeSpecification)specToClone).clone();
				}
				default: {
					throw new RuntimeException("Unexpected RDBD type: " + rdbdType);
				}
			}
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static RDBDSpecification initSpecification(int rdbdType) throws MalformedRelationIDException {
		RDBDSpecification spec;
		Relation relation;
		switch (rdbdType) {
			case (RDBDConstants.TYPE_NORMALFORM_DETERMINATION): {
				relation = new Relation();
				relation.setName("R");
				spec = relation;
				break;
			}
			case (RDBDConstants.TYPE_KEYS_DETERMINATION): {
				relation = new IdentifiedRelation();
				((IdentifiedRelation)relation).setID("0");
				relation.setName("R0");
				spec = relation;
				break;
			}
			case (RDBDConstants.TYPE_MINIMAL_COVER): {
				relation = new IdentifiedRelation();
				((IdentifiedRelation)relation).setID("0");
				relation.setName("R0");
				spec = relation;
				break;
			}
			case (RDBDConstants.TYPE_ATTRIBUTE_CLOSURE): {
				spec = new AttributeClosureSpecification();
				relation = new IdentifiedRelation();
				((IdentifiedRelation)relation).setID("0");
				relation.setName("R0");
				((AttributeClosureSpecification)spec).setBaseRelation((IdentifiedRelation)relation);
				break;
			}
			case (RDBDConstants.TYPE_RBR): {
				spec = new RBRSpecification();
				relation = new IdentifiedRelation();
				((IdentifiedRelation)relation).setID("0");
				relation.setName("R0");
				((RBRSpecification)spec).setBaseRelation((IdentifiedRelation)relation);
				break;
			}
			case (RDBDConstants.TYPE_NORMALIZATION): {
				spec = new NormalizationSpecification();
				relation = new IdentifiedRelation();
				((IdentifiedRelation)relation).setID("0");
				relation.setName("R0");
				((NormalizationSpecification)spec).setBaseRelation((IdentifiedRelation)relation);
				((NormalizationSpecification)spec).setMaxLostDependencies(0);
				((NormalizationSpecification)spec).setTargetLevel(NormalformLevel.THIRD);
				break;
			}
			case (RDBDConstants.TYPE_DECOMPOSE): {
				spec = new DecomposeSpecification();
				relation = new IdentifiedRelation();
				((IdentifiedRelation)relation).setID("1");
				relation.setName("R1");
				((DecomposeSpecification)spec).setBaseRelation((IdentifiedRelation)relation);
				((DecomposeSpecification)spec).setMaxLostDependencies(0);
				((DecomposeSpecification)spec).setTargetLevel(NormalformLevel.THIRD);
				break;
			}
			default: {
				throw new RuntimeException("Unexpected RDBD type: " + rdbdType);
			}
		}
		return spec;
	}

	
	public static SpecificationParser initParser(int rdbdType) {
		switch (rdbdType) {
			case (RDBDConstants.TYPE_NORMALFORM_DETERMINATION): {
				return new SpecificationParser("R", null, "F");
			}
			case (RDBDConstants.TYPE_KEYS_DETERMINATION): {
				return new SpecificationParser("R", null, "F");
			}
			case (RDBDConstants.TYPE_MINIMAL_COVER): {
				return new SpecificationParser("R", null, "F");
			}
			case (RDBDConstants.TYPE_ATTRIBUTE_CLOSURE): {
				return new SpecificationParser("R", "A", "F");
			}
			case (RDBDConstants.TYPE_RBR): {
				return new SpecificationParser("R", "S", "F");
			}
			case (RDBDConstants.TYPE_NORMALIZATION): {
				return new SpecificationParser("R", null, "F");
			}
			case (RDBDConstants.TYPE_DECOMPOSE): {
				return new SpecificationParser("R", null, "F");
			}
			default: {
				throw new RuntimeException("Unexpected RDBD type: " + rdbdType);
			}
		}
	}

	public static boolean isOfRdbdType(RDBDSpecification spec, int rdbdType) {
		if (spec == null) {
			return true;
		}
		switch (rdbdType) {
			case (RDBDConstants.TYPE_NORMALFORM_DETERMINATION): {
				return spec instanceof Relation;
			}
			case (RDBDConstants.TYPE_KEYS_DETERMINATION): {
				return spec instanceof IdentifiedRelation;
			}
			case (RDBDConstants.TYPE_MINIMAL_COVER): {
				return spec instanceof IdentifiedRelation;
			}
			case (RDBDConstants.TYPE_ATTRIBUTE_CLOSURE): {
				return spec instanceof AttributeClosureSpecification;
			}
			case (RDBDConstants.TYPE_RBR): {
				return spec instanceof RBRSpecification;
			}
			case (RDBDConstants.TYPE_NORMALIZATION): {
				return spec instanceof NormalizationSpecification;
			}
			case (RDBDConstants.TYPE_DECOMPOSE): {
				return spec instanceof DecomposeSpecification;
			}
			default: {
				throw new RuntimeException("Unexpected RDBD type: " + rdbdType);
			}
		}
	}
	
	public static String getAssignmentText(RDBDSpecification specTmp, int indent, Locale locale, int rdbdType) throws IOException {
		StringWriter writer = new StringWriter();
		switch (rdbdType) {
			case (RDBDConstants.TYPE_NORMALFORM_DETERMINATION): {
				writer.append(HTMLPrinter.printAssignmentForNormalformDetermination((Relation)specTmp, indent, locale));
				break;
			}
			case (RDBDConstants.TYPE_KEYS_DETERMINATION): {
				writer.append(HTMLPrinter.printAssignmentForKeysDetermination((IdentifiedRelation)specTmp, indent, locale));
				break;
			}
			case (RDBDConstants.TYPE_MINIMAL_COVER): {
				writer.append(HTMLPrinter.printAssignmentForMinimalCover((IdentifiedRelation)specTmp, indent, locale));
				break;
			}
			case (RDBDConstants.TYPE_ATTRIBUTE_CLOSURE): {
				writer.append(HTMLPrinter.printAssignmentForAttributeClosure((AttributeClosureSpecification)specTmp, indent, locale));
				break;
			}
			case (RDBDConstants.TYPE_RBR): {
				writer.append(HTMLPrinter.printAssignmentForRBR((RBRSpecification)specTmp, indent, locale));
				break;
			}
			case (RDBDConstants.TYPE_NORMALIZATION): {
				writer.append(HTMLPrinter.printAssignmentForNormalization((NormalizationSpecification)specTmp, indent, locale));
				break;
			}
			case (RDBDConstants.TYPE_DECOMPOSE): {
				writer.append(HTMLPrinter.printAssignmentForDecompose((DecomposeSpecification)specTmp, indent, locale));
				break;
			}
			default: {
				throw new RuntimeException("Unexpected RDBD type: " + rdbdType);
			}
		}
		return writer.toString();
	}
}