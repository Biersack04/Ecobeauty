package com.example.ecobeauty.mydeparture;

public interface RadioCheckable extends Checkable {
    void addOnCheckChangeListener(OnCheckedChangeListener onCheckedChangeListener);
    void removeOnCheckChangeListener(OnCheckedChangeListener onCheckedChangeListener);

    public static interface OnCheckedChangeListener {
        void onCheckedChanged(PresetValueButton radioGroup, boolean isChecked);
    }
}