package de.vogella.databinding.person.swt;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import de.vogella.databinding.person.model.Address;
import de.vogella.databinding.person.model.Person;

public class View extends ViewPart {
	public static final String ID = "de.vogella.databinding.person.swt.View";
	private Person person;

	private Text firstName;
	private Text ageText;
	private Button marriedButton;
	private Combo genderCombo;
	private Text countryText;

	@Override
	public void createPartControl(Composite parent) {

		person = new Person();
		Address address = new Address();
		address.setCountry("Deutschland");
		person.setAddress(address);
		person.setFirstName("John");
		person.setLastName("Doo");
		person.setGender("Male");
		person.setAge(12);
		person.setMarried(true);
		// Lets put thing to order
		Layout layout = new GridLayout(2, false);
		parent.setLayout(layout);

		Label firstLabel = new Label(parent, SWT.NONE);
		firstLabel.setText("Firstname: ");
		firstName = new Text(parent, SWT.BORDER);

		GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		firstName.setLayoutData(gridData);

		Label ageLabel = new Label(parent, SWT.NONE);
		ageLabel.setText("Age: ");
		ageText = new Text(parent, SWT.BORDER);

		gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		ageText.setLayoutData(gridData);

		Label marriedLabel = new Label(parent, SWT.NONE);
		marriedLabel.setText("Married: ");
		marriedButton = new Button(parent, SWT.CHECK);

		Label genderLabel = new Label(parent, SWT.NONE);
		genderLabel.setText("Gender: ");
		genderCombo = new Combo(parent, SWT.NONE);
		genderCombo.add("Male");
		genderCombo.add("Female");

		Label countryLabel = new Label(parent, SWT.NONE);
		countryLabel.setText("Country");
		countryText = new Text(parent, SWT.BORDER);

		Button button1 = new Button(parent, SWT.PUSH);
		button1.setText("Write model");
		button1.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Firstname: " + person.getFirstName());
				System.out.println("Age " + person.getAge());
				System.out.println("Married: " + person.isMarried());
				System.out.println("Gender: " + person.getGender());
				System.out.println("Country: "
						+ person.getAddress().getCountry());
			}
		});

		Button button2 = new Button(parent, SWT.PUSH);
		button2.setText("Change model");
		button2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				person.setFirstName("Lars");
				person.setAge(person.getAge() + 1);
				person.setMarried(!person.isMarried());
				if (person.getGender().equals("Male")) {

				} else {
					person.setGender("Male");
				}
				if (person.getAddress().getCountry().equals("Deutschland")) {
					person.getAddress().setCountry("USA");
				} else {
					person.getAddress().setCountry("Deutschland");
				}
			}
		});

		// Now lets do the binding
		bindValues();
	}

	@Override
	public void setFocus() {
	}

	private void bindValues() {
		// The DataBindingContext object will manage the databindings
		DataBindingContext bindingContext = new DataBindingContext();
		// Lets bind it
		IObservableValue widgetValue = WidgetProperties.text(SWT.Modify)
				.observe(firstName);
		IObservableValue modelValue = BeanProperties.value(Person.class,
				"firstName").observe(person);
		// The above factories are similar to the following statements
		// uiElement = SWTObservables.observeText(firstName, SWT.Modify);
		// modelElement = BeansObservables.observeValue(person, "firstName");
		// // The bindValue method call binds the text element with the model
		bindingContext.bindValue(widgetValue, modelValue);

		widgetValue = WidgetProperties.text(SWT.Modify).observe(ageText);
		modelValue = BeanProperties.value(Person.class, "age").observe(person);
		bindingContext.bindValue(widgetValue, modelValue);
		widgetValue = WidgetProperties.selection().observe(marriedButton);
		modelValue = BeanProperties.value(Person.class, "married").observe(
				person);
		bindingContext.bindValue(widgetValue, modelValue);

		widgetValue = WidgetProperties.selection().observe(genderCombo);
		modelValue = BeansObservables.observeValue(person, "gender");

		bindingContext.bindValue(widgetValue, modelValue);

		// Address field is bound to the Ui
		widgetValue = WidgetProperties.text(SWT.Modify).observe(ageText);
		modelValue = BeanProperties.value(Person.class, "address.country")
				.observe(person);
		bindingContext.bindValue(widgetValue, modelValue);

	}
}