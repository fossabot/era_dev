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
package era.foss.objecteditor.specobject;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.databinding.edit.EMFEditProperties;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import era.foss.erf.AttributeDefinitionSimple;
import era.foss.erf.AttributeValue;
import era.foss.erf.AttributeValueSimple;
import era.foss.erf.ErfPackage;
import era.foss.erf.SpecObject;
import era.foss.erf.ViewElement;
import era.foss.erf.impl.ErfFactoryImpl;
import era.foss.ui.contrib.TextAction;

/**
 * The Class AttributeDefinitionStringComposite.
 */
public class AttributeDefinitionStringComposite extends AbstractAttributeDefinitionComposite {

    /** The text control. */
    Text textControl;

    /** The default value modify listener. */
    private DefaultModifyListener defaultValueModifyListener;

    /**
     * Instantiates a new attribute definition string composite.
     *
     * @param parent the parent
     * @param viewElement the view element
     * @param specObject the spec object
     */
    public AttributeDefinitionStringComposite( Composite parent, ViewElement viewElement, SpecObject specObject ) {
        super( parent, viewElement );

    }

    public Control createControl() {
        int style = SWT.BORDER;
        if( viewElement.getEditorRowSpan() > 1 ) {
            style |= SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.H_SCROLL;
        }
        textControl = new Text( this, style );
        return textControl;
    }

    @Override
    public void doBind( SpecObject specObject, AttributeValue attributeValue, EditingDomain editingDomain ) {
        AttributeDefinitionSimple attributeDefinition = (AttributeDefinitionSimple)viewElement.getAttributeDefinition();

        if( attributeValue == null ) {
            for( Listener listener : textControl.getListeners( SWT.Modify ) ) {
                textControl.removeListener( SWT.Modify, listener );
            }

            if( attributeDefinition.getDefaultValue() != null ) {
                textControl.setText( attributeDefinition.getDefaultValue().getTheValue() );
                textControl.setBackground( Display.getDefault().getSystemColor( COLOR_DEFAULT_VALUE ) );
            } else {
                textControl.setText( "" );
                textControl.setBackground( Display.getDefault().getSystemColor( SWT.COLOR_WHITE ) );
            }
            defaultValueModifyListener = new DefaultModifyListener( specObject, editingDomain );
            textControl.addModifyListener( defaultValueModifyListener );

        } else {
            this.binding = dbc.bindValue( WidgetProperties.text( SWT.Modify ).observeDelayed( 400, textControl ),
                                          EMFEditProperties.value( editingDomain,
                                                                   ErfPackage.Literals.ATTRIBUTE_VALUE_SIMPLE__THE_VALUE )
                                                           .observe( attributeValue ) );

            textControl.setBackground( Display.getDefault().getSystemColor( SWT.COLOR_WHITE ) );
        }
    }

    @Override
    public void unbind() {

        if( defaultValueModifyListener != null ) {
            textControl.removeModifyListener( defaultValueModifyListener );
        }
        super.unbind();
    }

    /**
     * Listener whichs adds a AttributeValue to a SpecObject in case no.
     *
     * @see DefaultModifyEvent
     */
    private class DefaultModifyListener implements ModifyListener {

        /** Spec Object for which a AttributeValue has to be created in case the content of the Text control is changed. */
        SpecObject specObject;

        /** The editing domain. */
        EditingDomain editingDomain;

        public DefaultModifyListener( SpecObject specObject, EditingDomain editingDomain ) {
            super();
            this.specObject = specObject;
            this.editingDomain = editingDomain;
        }

        @Override
        public void modifyText( ModifyEvent e ) {
            AttributeDefinitionSimple attributeDefinition = (AttributeDefinitionSimple)viewElement.getAttributeDefinition();
            Text textControl = ((Text)e.widget);

            // as now a value is entered this listener is obsolete
            textControl.removeModifyListener( this );

            // create an Attribute Value
            AttributeValueSimple attributeValue = ErfFactoryImpl.eINSTANCE.createAttributeValueSimple();

            // set reference to the respective Attribute Definition
            attributeValue.setDefinition( attributeDefinition );
            // set value of attribute definition
            attributeValue.setTheValue( textControl.getText() );

            // create new Attribute value in the model
            Command cmd = AddCommand.create( editingDomain,
                                             specObject,
                                             ErfPackage.SPEC_OBJECT__VALUES,
                                             attributeValue );
            editingDomain.getCommandStack().execute( cmd );
        }
    }

    /* (non-Javadoc)
     * @see era.foss.objecteditor.AbstractAttributeDefinitionComposite#getControl()
     */
    @Override
    public Control getControl() {
        return textControl;
    }

    @Override
    protected void menuAboutToShow( IMenuManager manager ) {
        super.menuAboutToShow( manager );
        manager.add( new TextAction( textControl, TextAction.TextActionType.COPY ) );
        manager.add( new TextAction( textControl, TextAction.TextActionType.CUT ) );
        manager.add( new TextAction( textControl, TextAction.TextActionType.PASTE ) );
    }
}
