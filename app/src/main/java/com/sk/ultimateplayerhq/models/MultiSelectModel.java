package com.sk.ultimateplayerhq.models;

import java.util.Objects;

public class MultiSelectModel<T> {
    boolean isSelected = false;
    T item;

    public MultiSelectModel(boolean isSelected, T item) {
        this.isSelected = isSelected;
        this.item = item;
    }


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public T getItem() {
        return item;
    }

    public void setItem(T item) {
        this.item = item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MultiSelectModel)) return false;
        MultiSelectModel<?> that = (MultiSelectModel<?>) o;
        return Objects.equals(getItem(), that.getItem());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getItem());
    }
}
