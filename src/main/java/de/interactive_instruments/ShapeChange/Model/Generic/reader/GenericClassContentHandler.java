package de.interactive_instruments.ShapeChange.Model.Generic.reader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;
import java.util.Map.Entry;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import de.interactive_instruments.ShapeChange.Options;
import de.interactive_instruments.ShapeChange.ShapeChangeResult;
import de.interactive_instruments.ShapeChange.StructuredNumber;
import de.interactive_instruments.ShapeChange.Model.Constraint;
import de.interactive_instruments.ShapeChange.Model.Descriptors;
import de.interactive_instruments.ShapeChange.Model.ImageMetadata;
import de.interactive_instruments.ShapeChange.Model.PropertyInfo;
import de.interactive_instruments.ShapeChange.Model.Stereotypes;
import de.interactive_instruments.ShapeChange.Model.TaggedValues;
import de.interactive_instruments.ShapeChange.Model.Generic.GenericClassInfo;
import de.interactive_instruments.ShapeChange.Model.Descriptor;

public class GenericClassContentHandler
		extends AbstractGenericInfoContentHandler {

	private static final Set<String> SIMPLE_CLASS_FIELDS = new HashSet<String>(
			Arrays.asList(new String[] { "includePropertyType",
					"includeByValuePropertyType", "isCollection",
					"asDictionary", "asDictionaryGml33", "asGroup",
					"asCharacterString", "hasNilReason", "isAbstract", "isLeaf",
					"suppressed", "xmlSchemaType", "associationId",
					"baseClassId" }));

	private GenericClassInfo genCi = new GenericClassInfo();

	private String associationId = null;
	private String baseClassId = null;

	private List<GenericPropertyContentHandler> propertyContentHandlers = new ArrayList<GenericPropertyContentHandler>();
	private List<ConstraintContentHandler> constraintContentHandlers = new ArrayList<ConstraintContentHandler>();
	private ProfilesContentHandler profilesContentHandler = null;

	public GenericClassContentHandler(ShapeChangeResult result, Options options,
			XMLReader reader, AbstractContentHandler parent) {
		super(result, options, reader, parent);

		this.genCi.setResult(result);
		this.genCi.setOptions(options);
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {

		if (GenericModelReaderConstants.SIMPLE_INFO_FIELDS
				.contains(localName)) {

			sb = new StringBuffer();

		} else if (localName.equals("descriptors")) {

			DescriptorsContentHandler handler = new DescriptorsContentHandler(
					result, options, reader, this);

			super.descriptorsHandler = handler;
			reader.setContentHandler(handler);

		} else if (localName.equals("taggedValues")) {

			reader.setContentHandler(new TaggedValuesContentHandler(result,
					options, reader, this));

		} else if (localName.equals("stereotypes")) {

			reader.setContentHandler(new StringListContentHandler(result,
					options, reader, this));

		} else if (localName.equals("profiles")) {

			this.profilesContentHandler = new ProfilesContentHandler(result,
					options, reader, this);
			reader.setContentHandler(this.profilesContentHandler);

		} else if (localName.equals("diagrams")) {

			reader.setContentHandler(
					new DiagramsContentHandler(result, options, reader, this));

		} else if (SIMPLE_CLASS_FIELDS.contains(localName)) {

			sb = new StringBuffer();

		} else if (localName.equals("supertypes")) {

			reader.setContentHandler(new StringListContentHandler(result,
					options, reader, this));

		} else if (localName.equals("subtypes")) {

			reader.setContentHandler(new StringListContentHandler(result,
					options, reader, this));

		} else if (localName.equals("properties")) {

			// ignore

		} else if (localName.equals("Property")) {

			GenericPropertyContentHandler handler = new GenericPropertyContentHandler(
					result, options, reader, this);
			this.propertyContentHandlers.add(handler);
			reader.setContentHandler(handler);

		} else if (localName.equals("constraints")) {

			// ignore

		} else if (localName.equals("FolConstraint")
				|| localName.equals("OclConstraint")
				|| localName.equals("TextConstraint")) {

			ConstraintContentHandler handler = new ConstraintContentHandler(
					result, options, reader, this);
			this.constraintContentHandlers.add(handler);
			reader.setContentHandler(handler);

		} else {

			// do not throw an exception, just log a warning - the schema could
			// have been extended
			result.addWarning(null, 30800, "GenericClassContentHandler",
					localName);
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		if (localName.equals("id")) {

			this.genCi.setId(sb.toString());

			// String id = sb.toString();
			// // strip "_C" prefix added by ModelExport
			// id = id.substring(2);
			// this.genCi.setId(id);

		} else if (localName.equals("name")) {

			this.genCi.setName(sb.toString());

		} else if (localName.equals("stereotypes")) {

			Stereotypes stereotypesCache = options.stereotypesFactory();
			for (String stereotype : this.stringList) {
				stereotypesCache.add(stereotype);
			}
			this.genCi.setStereotypes(stereotypesCache);

		} else if (localName.equals("descriptors")) {

			/*
			 * ignore - we have a reference to the DescriptorsContentHandler
			 */

		} else if (localName.equals("taggedValues")) {

			/*
			 * ignore - TaggedValuesContentHandler calls
			 * this.setTaggedValues(...)
			 */

		} else if (localName.equals("profiles")) {

			this.genCi.setProfiles(this.profilesContentHandler.getProfiles());

		} else if (localName.equals("diagrams")) {

			/*
			 * ignore - DiagramsContentHandler calls this.setDiagrams(...)
			 */

		} else if (localName.equals("includePropertyType")) {

			this.genCi.setIncludePropertyType(toBooleanValue(sb));

		} else if (localName.equals("includeByValuePropertyType")) {

			this.genCi.setIncludeByValuePropertyType(toBooleanValue(sb));

		} else if (localName.equals("isCollection")) {

			this.genCi.setIsCollection(toBooleanValue(sb));

		} else if (localName.equals("asDictionary")) {

			this.genCi.setAsDictionary(toBooleanValue(sb));

		} else if (localName.equals("asDictionaryGml33")) {

			this.genCi.setAsDictionaryGml33(toBooleanValue(sb));

		} else if (localName.equals("asGroup")) {

			this.genCi.setAsGroup(toBooleanValue(sb));

		} else if (localName.equals("asCharacterString")) {

			this.genCi.setAsCharacterString(toBooleanValue(sb));

		} else if (localName.equals("hasNilReason")) {

			this.genCi.setHasNilReason(toBooleanValue(sb));

		} else if (localName.equals("isAbstract")) {

			this.genCi.setIsAbstract(toBooleanValue(sb));

		} else if (localName.equals("isLeaf")) {

			this.genCi.setIsLeaf(toBooleanValue(sb));

		} else if (localName.equals("suppressed")) {

			this.genCi.setSuppressed(toBooleanValue(sb));

		} else if (localName.equals("xmlSchemaType")) {

			this.genCi.setXmlSchemaType(sb.toString());

		} else if (localName.equals("associationId")) {

			this.associationId = sb.toString();

		}
		// else if (localName.equals("packageId")) {
		//
		// this.packageId = sb.toString();
		//
		// }
		else if (localName.equals("baseClassId")) {

			this.baseClassId = sb.toString();

		} else if (localName.equals("supertypes")) {

			this.genCi.setSupertypes(new TreeSet<String>(this.stringList));

		} else if (localName.equals("subtypes")) {

			this.genCi.setSubtypes(new TreeSet<String>(this.stringList));

		} else if (localName.equals("properties")) {

			// ignore

		} else if (localName.equals("Property")) {

			// ignore

		} else if (localName.equals("constraints")
				|| localName.equals("FolConstraint")
				|| localName.equals("OclConstraint")
				|| localName.equals("TextConstraint")) {

			// ignore

		} else if (localName.equals("Class")) {

			// set descriptors in genCi
			this.genCi.setDescriptors(descriptorsHandler.getDescriptors());
			// for (Entry<Descriptor, Descriptors> entry : descriptors
			// .getDescriptors().entrySet()) {
			//
			// if (entry.getKey() == Descriptor.ALIAS) {
			// this.genCi.setAliasNameAll(entry.getValue());
			// } else if (entry.getKey() == Descriptor.PRIMARYCODE) {
			// this.genCi.setPrimaryCodeAll(entry.getValue());
			// } else if (entry.getKey() == Descriptor.GLOBALIDENTIFIER) {
			// this.genCi.setGlobalIdentifierAll(entry.getValue());
			// }
			// // else if(entry.getKey() == Descriptor.DOCUMENTATION) {
			// // this.genCi.setDocumentationAll(entry.getValue());
			// // }
			// else if (entry.getKey() == Descriptor.DEFINITION) {
			// this.genCi.setDefinitionAll(entry.getValue());
			// } else if (entry.getKey() == Descriptor.DESCRIPTION) {
			// this.genCi.setDescriptionAll(entry.getValue());
			// } else if (entry.getKey() == Descriptor.LEGALBASIS) {
			// this.genCi.setLegalBasisAll(entry.getValue());
			// } else if (entry.getKey() == Descriptor.LANGUAGE) {
			// this.genCi.setLanguageAll(entry.getValue());
			// } else if (entry.getKey() == Descriptor.EXAMPLE) {
			// this.genCi.setExamplesAll(entry.getValue());
			// } else if (entry.getKey() == Descriptor.DATACAPTURESTATEMENT) {
			// this.genCi.setDataCaptureStatementsAll(entry.getValue());
			// }
			// }

			// set contained properties
			SortedMap<StructuredNumber, PropertyInfo> properties = new TreeMap<StructuredNumber, PropertyInfo>();
			for (GenericPropertyContentHandler gpch : this.propertyContentHandlers) {
				properties.put(gpch.getGenericProperty().sequenceNumber(),
						gpch.getGenericProperty());
			}
			this.genCi.setProperties(properties);

			// set contained constraints
			Vector<Constraint> cons = new Vector<Constraint>();
			for (ConstraintContentHandler cch : this.constraintContentHandlers) {
				cons.add(cch.getConstraint());
			}
			this.genCi.setConstraints(cons);

			// let parent know that we reached the end of the Class entry
			// (so that for example depth can properly be tracked)
			parent.endElement(uri, localName, qName);

			// Switch handler back to parent
			reader.setContentHandler(parent);

		} else {
			// do not throw an exception, just log a warning - the schema could
			// have been extended
			result.addWarning(null, 30801, "GenericClassContentHandler",
					localName);
		}
	}

	/**
	 * @return the genPi
	 */
	public GenericClassInfo getGenericClass() {
		return genCi;
	}

	@Override
	public void setTaggedValues(TaggedValues taggedValues) {

		this.genCi.setTaggedValues(taggedValues, false);
	}

	@Override
	public void setDiagrams(List<ImageMetadata> diagrams) {

		this.genCi.setDiagrams(diagrams);
	}

	/**
	 * @return the associationId
	 */
	public String getAssociationId() {
		return associationId;
	}

	// /**
	// * @return the packageId
	// */
	// public String getPackageId() {
	// return packageId;
	// }

	/**
	 * @return the baseClassId
	 */
	public String getBaseClassId() {
		return baseClassId;
	}

	/**
	 * @return the propertyContentHandlers
	 */
	public List<GenericPropertyContentHandler> getPropertyContentHandlers() {
		return propertyContentHandlers;
	}

	/**
	 * @return the constraintContentHandlers
	 */
	public List<ConstraintContentHandler> getConstraintContentHandlers() {
		return constraintContentHandlers;
	}

}
