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
 * (c) 2002-2017 interactive instruments GmbH, Bonn, Germany
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
package de.interactive_instruments.ShapeChange.Profile;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Pattern;

import de.interactive_instruments.ShapeChange.Model.ClassInfo;
import de.interactive_instruments.ShapeChange.Model.Model;
import de.interactive_instruments.ShapeChange.Model.PackageInfo;
import de.interactive_instruments.ShapeChange.Model.PropertyInfo;
import de.interactive_instruments.ShapeChange.Model.Generic.GenericClassInfo;
import de.interactive_instruments.ShapeChange.Model.Generic.GenericModel;
import de.interactive_instruments.ShapeChange.Model.Generic.GenericPropertyInfo;

/**
 * @author Johannes Echterhoff (echterhoff <at> interactive-instruments
 *         <dot> de)
 *
 */
public class ProfileUtil {

	/**
	 * @param model
	 * @return Set with names of all profiles defined by elements of the given
	 *         model. Can be empty but not <code>null</code>.
	 */
	public static SortedSet<String> findNamesOfAllProfiles(Model model) {

		SortedSet<String> result = new TreeSet<String>();

		if (model != null) {

			for (PackageInfo pkg : model.packages()) {

				for (ClassInfo ci : pkg.containedClasses()) {

					result.addAll(ci.profiles().getProfileIdentifiersByName()
							.keySet());

					for (PropertyInfo pi : ci.properties().values()) {

						result.addAll(pi.profiles()
								.getProfileIdentifiersByName().keySet());
					}
				}
			}
		}

		return result;
	}

	/**
	 * @param model
	 *            The model whose profile definitions are not explicit (i.e.
	 *            classes without profile definitions belong to all profiles,
	 *            and properties without profile definitions inherit the
	 *            profiles of their class). Must not be <code>null</code>.
	 * @param profilesForClassesBelongingToAllProfiles
	 *            Set of profiles that shall be assigned to classes that belong
	 *            to all profiles (i.e., they do not have any profile definition
	 *            in the given model, which is what this method is meant to
	 *            convert into a set of explicit profile definitions). Must not
	 *            be <code>null</code>.
	 * @param schemaNameRegex
	 *            Regular expression to match the name of schemas in which the
	 *            profile definitions shall be converted to explicit ones. If
	 *            this parameter is <code>null</code>, the conversion will be
	 *            applied to all classes and properties of the model.
	 * @return A copy of the given model, with profile definitions converted to
	 *         be explicit. NOTE: The returned model has not been postprocessed;
	 *         if required (e.g. to ensure constraints have been parsed and
	 *         validated), this must be performed outside of this method.
	 */
	public static GenericModel convertToExplicitProfileDefinitions(Model model,
			Profiles profilesForClassesBelongingToAllProfiles,
			Pattern schemaNameRegex) {

		/*
		 * Create model copy, convert profile definitions in that copy, and
		 * return it
		 */
		GenericModel genModel = new GenericModel(model);

		Set<GenericClassInfo> genCisToProcess = new HashSet<GenericClassInfo>();

		for (GenericClassInfo genCi : genModel.getGenClasses().values()) {

			if (schemaNameRegex != null) {

				PackageInfo schemaPkg = genModel.schemaPackage(genCi);

				if (schemaPkg != null && schemaNameRegex
						.matcher(schemaPkg.name()).matches()) {
					genCisToProcess.add(genCi);
				}

			} else {

				genCisToProcess.add(genCi);

				/*
				 * Note: We cannot ignore the class even if it defines profiles,
				 * since one if its properties may not do so.
				 */
			}
		}

		for (GenericClassInfo genCi : genCisToProcess) {

			if (genCi.profiles().isEmpty()) {

				genCi.setProfiles(
						profilesForClassesBelongingToAllProfiles.createCopy());
			}

			for (PropertyInfo pi : genCi.properties().values()) {

				if (pi.profiles().isEmpty()) {

					/*
					 * Use a copy of the profiles defined by the class
					 */
					GenericPropertyInfo genPi = (GenericPropertyInfo) pi;
					genPi.setProfiles(genCi.profiles().createCopy());
				}
			}
		}

		return genModel;
	}
}
