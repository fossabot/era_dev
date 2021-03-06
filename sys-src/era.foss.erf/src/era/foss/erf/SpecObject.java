/**************************************************************************
 * ERA - Eclipse Requirements Analysis
 * ==============================================
 * Copyright (C) 2009-2013 by Georg Blaschke, Christoph P. Neumann
 * and Bernd Haberstumpf (http://era.origo.ethz.ch)
 **************************************************************************
 * Licensed under the Eclipse Public License - v 1.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.eclipse.org/org/documents/epl-v10.html
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **************************************************************************
 */
package era.foss.erf;

import org.eclipse.emf.common.util.EList;

/**
 * The Interface SpecObject.
 * 
 * @model
 */
public interface SpecObject extends SpecElementWithUserDefinedAttributes {

    /**
     * Gets the sources.
     * 
     * @return the sources
     * @model kind="reference" opposite="target" required="false"
     */
    public EList<SpecRelation> getSources();

    /**
     * Gets the targets.
     * 
     * @return the targets
     * @model kind="reference" opposite="source" required="false"
     */
    public EList<SpecRelation> getTargets();

    /**
     * Gets the the owning ReqIFContent element.
     * 
     * @return the core content of the specification
     * @model required="true"
     */
    public Content getCoreContent();

    /**
     * Sets the value of the '{@link era.foss.erf.SpecObject#getCoreContent <em>Core Content</em>}' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Core Content</em>' container reference.
     * @see #getCoreContent()
     * @generated
     */
    void setCoreContent( Content value );

    /**
     * Gets the targets.
     * 
     * @return the targets
     * @model kind="reference" opposite="object" required="false"
     */
    public SpecHierarchy getSpecHierarchy();

    /**
     * Sets the value of the '{@link era.foss.erf.SpecObject#getSpecHierarchy <em>Spec Hierarchy</em>}' reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value the new value of the '<em>Spec Hierarchy</em>' reference.
     * @see #getSpecHierarchy()
     * @generated
     */
    void setSpecHierarchy( SpecHierarchy value );

}
