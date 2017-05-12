/**
 * ShapeChange - processing application schemas for geographic information
 *
 * This file is part of ShapeChange. ShapeChange takes a ISO 19109 
 * Application Schema from a UML model and translates it into a 
 * GML Application Schema or other implementation representations.
 *
 * Additional information about the software can be found at
 * http://shapechange.net/
 *
 * (c) 2002-2015 interactive instruments GmbH, Bonn, Germany
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact:
 * interactive instruments GmbH
 * Trierer Strasse 70-72
 * 53115 Bonn
 * Germany
 */
package de.interactive_instruments.ShapeChange;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.interactive_instruments.ShapeChange.ShapeChangeResult.MessageContext;

/**
 * Parses information from the 'param' attribute of a map entry and provides
 * access to the resulting information.
 * 
 * @author Johannes Echterhoff (echterhoff <at> interactive-instruments
 *         <dot> de)
 *
 */
public class MapEntryParamInfos implements MessageSource {

	/**
	 * Regular expression
	 * ((\w+)(\{(([^;=\}]+)=?((?<==)[^;=\}]+)?[\s;]*)+\})?[\s,]*)+ to match
	 * valid values of the 'param' attribute in a map entry.
	 * 
	 * Examples of valid values are:
	 * <ul>
	 * <li>paramX</li>
	 * <li>paramX{a=xyz;b=42,c}</li>
	 * <li>paramX{a=xyz},paramY</li>
	 * <li>paramX{a=xyz},paramY{d=(80.2,20.4)}</li>
	 * <li>paramX{a=xyz},paramY{d=(80.2,20.4)},paramZ{d}</li>
	 * </ul>
	 */
	public static final String PARAM_VALIDATION_PATTERN = "((\\w+)(\\{(([^;=\\}]+)=?((?<==)[^;=\\}]+)?[\\s;]*)+\\})?[\\s,]*)+";

	/**
	 * Regular expression (\w+)\{?((?<=\{)[^\}]+(?=\}))?\}?[\s;]* to find
	 * individual parameters contained in the value of the 'param' attribute in
	 * a map entry, together with their characteristics (given inside curly
	 * braces: {&lt;characteristics&gt;}).
	 * 
	 * Upon each find(), the parameter name is contained in group 1 while group
	 * 2 either contains the characteristics or is <code>null</code> if none are
	 * provided for the parameter.
	 */
	public static final String PARAMETER_IDENTIFICATION_PATTERN = "(\\w+)\\{?((?<=\\{)[^\\}]+(?=\\}))?\\}?[\\s;]*";

	/**
	 * Regular expression ([^;=]+)=?((?<==)[^;=]+)?[\s;]* to parse the
	 * individual characteristics of a parameter. Characteristics are separated
	 * by semicolon, and consist of an identifier and an optional value
	 * (separated by '=').
	 * 
	 * Upon each find(), the identifier of the characteristic is contained in
	 * group 1 while group 2 either contains the value or is <code>null</code>
	 * if none is provided for the characteristic.
	 */
	public static final String CHARACTERISTICS_IDENTIFICATION_PATTERN = "([^;=]+)=?((?<==)[^;=]+)?[\\s;]*";

	private Pattern validationPattern;
	private Pattern paramIdentPattern;
	private Pattern charactIdentPattern;

	/**
	 * key: {map entry type name}#{map entry rule name}
	 * 
	 * value: sub-map with key: parameter name, value: subsub-map with key:
	 * identifier of characteristic, value: value of characteristic (can be
	 * <code>null</code>).
	 * 
	 * If no non-empty and valid parameter value is provided for a type defined
	 * in a type mapping, then no information will be contained for that type in
	 * the map.
	 */
	private Map<String, Map<String, Map<String, String>>> paramCache;
	private Collection<ProcessMapEntry> mapEntries;

	private ShapeChangeResult result;
	private Options options;

	private boolean allParamsInMapEntriesAreValid = true;

	public MapEntryParamInfos(ShapeChangeResult result,
			Collection<ProcessMapEntry> pmes) {

		this.result = result;
		this.options = result.options();

		this.validationPattern = Pattern.compile(PARAM_VALIDATION_PATTERN);
		this.paramIdentPattern = Pattern
				.compile(PARAMETER_IDENTIFICATION_PATTERN);
		this.charactIdentPattern = Pattern
				.compile(CHARACTERISTICS_IDENTIFICATION_PATTERN);

		this.mapEntries = pmes;
		paramCache = new HashMap<String, Map<String, Map<String, String>>>();

		if (pmes != null) {

			for (ProcessMapEntry pme : pmes) {

				if (pme.hasParam()) {

					String param = pme.getParam();
					String pmeKey = pme.getType() + "#" + pme.getRule();

					/*
					 * validate the param value - continue parsing only if a
					 * match was detected
					 */
					Matcher vm = validationPattern.matcher(param);

					if (vm.matches()) {

						if (paramCache.containsKey(pmeKey)) {

							MessageContext mc = result.addWarning(this, 4);
							mc.addDetail(this, 1, pme.getType(), pme.getRule(),
									param);

						} else {

							Map<String, Map<String, String>> charactByParameterName = new HashMap<String, Map<String, String>>();

							paramCache.put(pmeKey, charactByParameterName);

							/*
							 * Find and add all individual parameters defined
							 * for the 'param' attribute.
							 */
							Matcher pim = paramIdentPattern.matcher(param);

							while (pim.find()) {

								String parameterName = pim.group(1);

								Map<String, String> characteristics = new HashMap<String, String>();
								charactByParameterName.put(parameterName,
										characteristics);

								String parameterValue = pim.group(2);

								if (parameterValue != null) {

									/*
									 * Find and add all individual
									 * characteristics.
									 */
									Matcher cim = charactIdentPattern
											.matcher(parameterValue);

									while (cim.find()) {

										String characteristicId = cim.group(1);
										String characteristicValue = cim
												.group(2);

										characteristics.put(characteristicId,
												characteristicValue);
									}
								}
							}
						}

					} else {

						allParamsInMapEntriesAreValid = false;

						MessageContext mc = result.addError(this, 3);
						mc.addDetail(this, 1, pme.getType(), pme.getRule(),
								param);
					}
				}
			}
		}
	}

	public boolean hasParameter(String typeName, String encodingRule,
			String parameterName) {

		/*
		 * Identify relevant map entry based upon the encoding rule (may require
		 * a look up through extended rules, which Options handles for us).
		 */
		ProcessMapEntry relevantMapEntry = options.targetMapEntry(typeName,
				encodingRule);

		return hasParameter(relevantMapEntry, parameterName);
	}

	public boolean hasParameter(ProcessMapEntry pme, String parameterName) {

		if (pme == null) {
			return false;
		} else {
			String pmeKey = pme.getType() + "#" + pme.getRule();
			if (paramCache.containsKey(pmeKey)
					&& paramCache.get(pmeKey).containsKey(parameterName)) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean hasCharacteristic(String typeName, String encodingRule,
			String parameter, String characteristic) {

		Map<String, String> characteristics = this.getCharacteristics(typeName,
				encodingRule, parameter);

		if (characteristics == null) {
			return false;
		} else {
			if (characteristics.containsKey(characteristic)) {
				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * @param typeName
	 * @param parameter
	 * @return A map with the characteristics defined for the parameter in the
	 *         map entry that applies to the named type under the given encoding
	 *         rule, or <code>null</code> if they don't exist.
	 */
	public Map<String, String> getCharacteristics(String typeName,
			String encodingRule, String parameter) {

		/*
		 * Identify relevant map entry based upon the encoding rule (may require
		 * a look up through extended rules, which Options handles for us).
		 */
		ProcessMapEntry relevantMapEntry = options.targetMapEntry(typeName,
				encodingRule);

		/*
		 * If we were able to identify the relevant map entry, we can grab the
		 * parameter info (if @param was set for that map entry).
		 */

		if (relevantMapEntry == null) {

			return null;

		} else {

			String pmeKey = typeName + "#" + relevantMapEntry.rule;

			if (!paramCache.containsKey(pmeKey)) {

				// then no valid parameter was defined in the map entry
				return null;

			} else {

				return paramCache.get(pmeKey).get(parameter);
			}
		}
	}

	public ProcessMapEntry getMapEntry(String typeName, String encodingRule) {

		/*
		 * Identify relevant map entry based upon the encoding rule (may require
		 * a look up through extended rules, which Options handles for us).
		 */
		ProcessMapEntry relevantMapEntry = options.targetMapEntry(typeName,
				encodingRule);

		/*
		 * If we were able to identify the relevant map entry, we can grab the
		 * process map entry.
		 */

		if (relevantMapEntry == null) {
			return null;
		} else {
			String pmeKey = typeName + "#" + relevantMapEntry.rule;
			for (ProcessMapEntry pme : this.mapEntries) {
				if (pmeKey.equalsIgnoreCase(
						pme.getType() + "#" + pme.getRule())) {
					return pme;
				}
			}
			return null;
		}
	}

	/**
	 * @param typeName
	 * @param parameter
	 * @param characteristic
	 * @return The value of the characteristic of the parameter in the map entry
	 *         for the named type, can be <code>null</code> if no value was
	 *         provided or if the characteristic was not specified for the
	 *         parameter.
	 */
	public String getCharacteristic(String typeName, String encodingRule,
			String parameter, String characteristic) {

		Map<String, String> characteristics = this.getCharacteristics(typeName,
				encodingRule, parameter);

		if (characteristics == null) {
			return null;
		} else {
			return characteristics.get(characteristic);
		}
	}

	public Map<String, Map<String, Map<String, String>>> getParameterCache() {
		return paramCache;
	}

	public boolean isValid() {
		return allParamsInMapEntriesAreValid;
	}

	@Override
	public String message(int mnr) {

		switch (mnr) {
		case 1:
			return "Context: map entry with @type='$1$', @rule='$2$' and @param='$3$'.";
		case 2:
			return "";
		case 3:
			return "Found invalid value for 'param' attribute in map entry. The entry will be ignored. Ensure that the value matches the regular expression "
					+ PARAM_VALIDATION_PATTERN + ".";
		case 4:
			return "Found another map entry with 'param' attribute for a type/rule mapping for which a 'param' attribute has already been parsed. The 'param' value of the additional map entry will be ignored.";

		default:
			return "(Unknown message)";
		}
	}
}
