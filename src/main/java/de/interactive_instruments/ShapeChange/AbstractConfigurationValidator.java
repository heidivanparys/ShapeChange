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
 * (c) 2002-2020 interactive instruments GmbH, Bonn, Germany
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

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.apache.commons.text.similarity.LevenshteinDistance;

import de.interactive_instruments.ShapeChange.Target.Target;
import de.interactive_instruments.ShapeChange.Target.TargetOutputProcessor;
import de.interactive_instruments.ShapeChange.Transformation.TransformationManager;

/**
 * @author Johannes Echterhoff (echterhoff at interactive-instruments dot de)
 *
 */
public abstract class AbstractConfigurationValidator implements ConfigurationValidator, MessageSource {

    protected LevenshteinDistance levDistance = new LevenshteinDistance(3);
    protected boolean reportInvalidParameterAsError = false;

    /**
     * Checks if all relevant parameters from the ShapeChange configuration belong
     * to allowed parameters.
     * 
     * @param allowedParametersWithStaticNames          names of allowed process
     *                                                  specific parameters; can be
     *                                                  <code>null</code>
     * @param regexForAllowedParametersWithDynamicNames can be <code>null</code>
     * @param actualParameters                          names of parameters
     *                                                  contained in the
     *                                                  configuration, to be
     *                                                  validated; can be
     *                                                  <code>null</code>
     * @param result                                    for reporting any parameter
     *                                                  that is not allowed
     * @return <code>true</code> if all actual parameters belong to allowed
     *         parameters, else <code>false</code>
     */
    public boolean validateParameters(SortedSet<String> allowedParametersWithStaticNames,
	    Pattern regexForAllowedParametersWithDynamicNames, Set<String> actualParameters, ShapeChangeResult result) {

	boolean allParametersValid = true;

	if (actualParameters != null) {

	    for (String parameter : actualParameters) {

		boolean isAllowed = false;

		if (allowedParametersWithStaticNames != null) {
		    isAllowed = allowedParametersWithStaticNames.contains(parameter);
		}

		if (!isAllowed && regexForAllowedParametersWithDynamicNames != null) {
		    isAllowed = regexForAllowedParametersWithDynamicNames.matcher(parameter).matches();
		}

		if (!isAllowed) {

		    allParametersValid = false;

		    // report the invalid parameter

		    /*
		     * check if the string distance of the parameter is near to one of the allowed
		     * parameters
		     */
		    String allowedParameterWithNearStringDistance = null;

		    if (allowedParametersWithStaticNames != null) {
			for (String allowedParameter : allowedParametersWithStaticNames) {
			    if (levDistance.apply(parameter, allowedParameter) != -1) {
				allowedParameterWithNearStringDistance = allowedParameter;
				break;
			    }
			}
		    }

		    if (allowedParameterWithNearStringDistance != null) {
			if (reportInvalidParameterAsError) {
			    result.addError(null, 1000000, parameter, allowedParameterWithNearStringDistance);
			} else {
			    result.addWarning(null, 1000000, parameter, allowedParameterWithNearStringDistance);
			}
		    } else {
			if (reportInvalidParameterAsError) {
			    result.addError(null, 1000001, parameter);
			} else {
			    result.addWarning(null, 1000001, parameter);
			}
		    }

		}
	    }
	}

	return !reportInvalidParameterAsError || allParametersValid;
    }

    public SortedSet<String> getCommonTransformerParameters() {
	return TransformationManager.getRecognizedParameters();
    }

    public SortedSet<String> getCommonTargetParameters() {
	SortedSet<String> result = new TreeSet<>();
	result.addAll(Target.COMMON_TARGET_PARAMETERS);
	result.addAll(TargetOutputProcessor.getRecognizedParameters());
	return result;
    }
}
