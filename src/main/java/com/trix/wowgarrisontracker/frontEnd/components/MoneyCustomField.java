package com.trix.wowgarrisontracker.frontEnd.components;

import com.trix.wowgarrisontracker.pojos.Money;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;

public class MoneyCustomField extends CustomField<Money> {

    private final IntegerField goldField;
    private final IntegerField silverField;
    private final IntegerField copperField;


    public MoneyCustomField(String label) {

        setLabel(label);
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        goldField = createIntegerField("Gold", 100_000_000);
        silverField = createIntegerField("Silver", 99);
        copperField = createIntegerField("Copper", 99);

        horizontalLayout.add(goldField,silverField,copperField);
        add(horizontalLayout);

    }

    private IntegerField createIntegerField(String text, int max) {
        IntegerField field = new IntegerField();
        field.setPlaceholder(text);
        field.setLabel(text);
        field.setMin(0);
        field.setMax(max);
        field.setStep(1);
        field.setHasControls(true);
        field.setValue(0);
        field.getStyle().set("flex","1");
        return field;
    }


    @Override
    protected Money generateModelValue() {
        return new Money(goldField.getValue(), silverField.getValue(), copperField.getValue());
    }

    @Override
    protected void setPresentationValue(Money money) {
        if(money != null) {
            int[] values = money.getFormattedValues();
            goldField.setValue(values[0]);
            silverField.setValue(values[1]);
            copperField.setValue(values[2]);
        }
    }
}
