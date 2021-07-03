package com.trix.wowgarrisontracker.frontEnd.components;

import com.trix.wowgarrisontracker.pojos.Money;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.IntegerField;

import static com.vaadin.flow.component.formlayout.FormLayout.*;

public class MoneyCustomField extends CustomField<Money> {

    private final IntegerField goldField;
    private final IntegerField silverField;
    private final IntegerField copperField;


    public MoneyCustomField(String label) {

        setLabel(label);
        FormLayout formLayout = new FormLayout();
        formLayout.setWidthFull();
        formLayout.setResponsiveSteps( new ResponsiveStep("1px",1),
                new ResponsiveStep("300px", 3));

        goldField = createIntegerField("Gold", 100_000_000);
        silverField = createIntegerField("Silver", 99);
        copperField = createIntegerField("Copper", 99);

        formLayout.add(goldField, silverField, copperField);
        add(formLayout);

    }

    private IntegerField createIntegerField(String text, int max) {
        IntegerField field = new IntegerField();
        field.setPlaceholder(text);
        field.setMin(0);
        field.setMax(max);
        field.setStep(1);
        field.setHasControls(true);
        field.setValue(0);
//        field.getStyle().set("flex", "1");
        field.setHelperText(text);
        return field;
    }


    @Override
    protected Money generateModelValue() {
        if (goldField.getValue() == null || silverField.getValue() == null || copperField.getValue() == null)
            return null;
        return new Money(goldField.getValue(), silverField.getValue(), copperField.getValue());
    }

    @Override
    protected void setPresentationValue(Money money) {
        if (money != null) {
            int[] values = money.getFormattedValues();
            goldField.setValue(values[0]);
            silverField.setValue(values[1]);
            copperField.setValue(values[2]);
        } else {
            goldField.setValue(0);
            silverField.setValue(0);
            copperField.setValue(0);
        }
    }
}
