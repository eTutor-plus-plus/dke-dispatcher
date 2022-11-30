package at.jku.dke.etutor.modules.pm.plg.io.importer;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;

import com.google.common.base.CharMatcher;

import at.jku.dke.etutor.modules.pm.plg.annotations.Importer;
import at.jku.dke.etutor.modules.pm.plg.generator.IProgressVisualizer;
import at.jku.dke.etutor.modules.pm.plg.generator.scriptexecuter.IntegerScriptExecutor;
import at.jku.dke.etutor.modules.pm.plg.generator.scriptexecuter.StringScriptExecutor;
import at.jku.dke.etutor.modules.pm.plg.model.Component;
import at.jku.dke.etutor.modules.pm.plg.model.FlowObject;
import at.jku.dke.etutor.modules.pm.plg.model.Process;
import at.jku.dke.etutor.modules.pm.plg.model.activity.Task;
import at.jku.dke.etutor.modules.pm.plg.model.data.DataObject;
import at.jku.dke.etutor.modules.pm.plg.model.data.IDataObjectOwner;
import at.jku.dke.etutor.modules.pm.plg.model.data.IDataObjectOwner.DATA_OBJECT_DIRECTION;
import at.jku.dke.etutor.modules.pm.plg.model.data.IntegerDataObject;
import at.jku.dke.etutor.modules.pm.plg.model.data.StringDataObject;
import at.jku.dke.etutor.modules.pm.plg.model.event.EndEvent;
import at.jku.dke.etutor.modules.pm.plg.model.event.StartEvent;
import at.jku.dke.etutor.modules.pm.plg.model.gateway.ExclusiveGateway;
import at.jku.dke.etutor.modules.pm.plg.model.gateway.ParallelGateway;
import at.jku.dke.etutor.modules.pm.plg.model.sequence.Sequence;

/**
 * This importing module works with process models represented as BPMN. In
 * particular, this class imports from XML files expresses using the
 * {@link http://schema.omg.org/spec/BPMN/2.0} or
 * {@link http://www.omg.org/spec/BPMN/20100524/MODEL}.
 * 
 * <p>
 * This class imports:
 * <ul>
 * 	<li>start and end events;</li>
 * 	<li>tasks;</li>
 * 	<li>AND and XOR gateways;</li>
 * 	<li>data objects.</li>
 * </ul>
 * 
 * @author Andrea Burattin
 */
@Importer(
	name = "BPMN files (from Signavio or Oryx)",
	fileExtension = "bpmn"
)
public class BPMNImporter extends FileImporter {

	private static final Namespace nsSignavio = Namespace.getNamespace("http://www.omg.org/spec/BPMN/20100524/MODEL");
	private static final Namespace nsOryx = Namespace.getNamespace("http://schema.omg.org/spec/BPMN/2.0");
	private static final Pattern REGEX_SIMPLE = Pattern.compile("(\\S+)\\s*=\\s*(\\S+)");
	private static final Pattern REGEX_INTEGER_SCRIPT = Pattern.compile("(?i)\\s*(\\S+)\\s*\\(\\s*Integer\\s*\\)\\s*");
	private static final Pattern REGEX_STRING_SCRIPT = Pattern.compile("(?i)\\s*(\\S+)\\s*\\(\\s*String\\s*\\)\\s*");
	
	@Override
	public Process importModel(String filename, IProgressVisualizer progress) {
		progress.setText("Importing Signavio BPMN file...");
		progress.setMinimum(0);
		progress.setMaximum(7);
		progress.start();
		
		HashMap<String, Component> inverseKey = new HashMap<String, Component>();
		HashMap<String, Set<Task>> taskToDataObject = new HashMap<String, Set<Task>>();
		HashMap<String, Set<Task>> dataObjectToTask = new HashMap<String, Set<Task>>();
		
		Process p = null;
		try {
			FileInputStream input = new FileInputStream(filename);
			
			SAXBuilder builder = new SAXBuilder();
			Document document = (Document) builder.build(input);
			Element definitions = document.getRootElement();
			
			// set correct namespace
			Namespace ns = nsSignavio;
			if (definitions.getChild("process", ns) == null) {
				ns = nsOryx;
			}
			
			Element process = definitions.getChild("process", ns);
			p = new Process(process.getAttributeValue("id"));
			
			// Events (start and end)
			for (Element ss : process.getChildren("startEvent", ns)) {
				StartEvent s = new StartEvent(p);
				inverseKey.put(ss.getAttributeValue("id"), s);
			}
			progress.inc();
			for (Element es : process.getChildren("endEvent", ns)) {
				EndEvent e = new EndEvent(p);
				inverseKey.put(es.getAttributeValue("id"), e);
			}
			progress.inc();
			// Tasks
			for (Element ts : process.getChildren("task", ns)) {
				Task t = new Task(p, ts.getAttributeValue("name"));
				String script = ts.getChildText("documentation", ns);
				if (script != null) {
					t.setActivityScript(new IntegerScriptExecutor(script));
				}
				inverseKey.put(ts.getAttributeValue("id"), t);
				for (Element doa : ts.getChildren("dataOutputAssociation", ns)) {
					Set<Task> s = new HashSet<Task>();
					if (taskToDataObject.containsKey(doa.getChildText("targetRef", ns))) {
						s = taskToDataObject.get(doa.getChildText("targetRef", ns));
					}
					s.add(t);
					taskToDataObject.put(doa.getChildText("targetRef", ns), s);
				}
				for (Element doa : ts.getChildren("dataInputAssociation", ns)) {
					Set<Task> s = new HashSet<Task>();
					if (dataObjectToTask.containsKey(doa.getChildText("sourceRef", ns))) {
						s = dataObjectToTask.get(doa.getChildText("sourceRef", ns));
					}
					s.add(t);
					dataObjectToTask.put(doa.getChildText("sourceRef", ns), s);
				}
			}
			progress.inc();
			// Gateways
			for (Element gs : process.getChildren("exclusiveGateway", ns)) {
				ExclusiveGateway g = new ExclusiveGateway(p);
				inverseKey.put(gs.getAttributeValue("id"), g);
			}
			progress.inc();
			for (Element gs : process.getChildren("parallelGateway", ns)) {
				ParallelGateway g = new ParallelGateway(p);
				inverseKey.put(gs.getAttributeValue("id"), g);
			}
			progress.inc();
			// Sequences
			for (Element ss : process.getChildren("sequenceFlow", ns)) {
				FlowObject source = (FlowObject) inverseKey.get(ss.getAttributeValue("sourceRef"));
				FlowObject sink = (FlowObject) inverseKey.get(ss.getAttributeValue("targetRef"));
				new Sequence(p, source, sink);
			}
			progress.inc();
			// Data Objects
			for (Element ds : process.getChildren("dataObjectReference", ns)) {
				String doId = ds.getAttributeValue("id");
				if (taskToDataObject.containsKey(doId)) {
					for(Task t : taskToDataObject.get(doId)) {
						parseDataObject(ds, t, DATA_OBJECT_DIRECTION.GENERATED, p, ns);
					}
				} else if (dataObjectToTask.containsKey(doId)) {
					for(Task t : dataObjectToTask.get(doId)) {
						parseDataObject(ds, t, DATA_OBJECT_DIRECTION.REQUIRED, p, ns);
					}
					/*for(Task t : dataObjectToTask.get(doId)) {
						for (FlowObject fo : t.getIncomingObjects()) {
							Sequence s = p.getSequence(fo, t);
							parseDataObject(ds, s, p);
						}
					}*/
				} else {
					parseDataObject(ds, null, null, p, ns);
				}
			}
			progress.inc();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		progress.finished();
		return p;
	}
	
	/**
	 * Method that parses a data object XML structure to generate the logic
	 * business object
	 * 
	 * @param dataObjectElement
	 * @param owner
	 * @param direction
	 * @param process
	 * @return
	 */
	private DataObject parseDataObject(Element dataObjectElement, IDataObjectOwner owner, DATA_OBJECT_DIRECTION direction, Process process, Namespace ns) {
		DataObject dataObject = null;
		String name = dataObjectElement.getAttributeValue("name");
		Matcher matcherSimple = REGEX_SIMPLE.matcher(name);
		Matcher matcherIntegerScript = REGEX_INTEGER_SCRIPT.matcher(name);
		Matcher matcherStringScript = REGEX_STRING_SCRIPT.matcher(name);
		
		if (matcherSimple.matches()) {
			dataObject = new DataObject(process);
			dataObject.setName(matcherSimple.group(1));
			dataObject.setValue(matcherSimple.group(2));
		} else if (matcherIntegerScript.matches()) {
			String script = CharMatcher.ASCII.retainFrom(dataObjectElement.getChildText("documentation", ns));
			dataObject = new IntegerDataObject(process, new IntegerScriptExecutor(script));
			dataObject.setName(matcherIntegerScript.group(1));
		} else if (matcherStringScript.matches()) {
			String script = CharMatcher.ASCII.retainFrom(dataObjectElement.getChildText("documentation", ns));
			dataObject = new StringDataObject(process, new StringScriptExecutor(script));
			dataObject.setName(matcherStringScript.group(1));
		}
		
		if (owner != null) {
			dataObject.setObjectOwner(owner, direction);
		}
		
		return dataObject;
	}
}
