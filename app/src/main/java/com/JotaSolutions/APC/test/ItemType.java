

package com.JotaSolutions.APC.test;



public enum ItemType {LOAD(10), ITEM(11);
    private final int typeCode;

    ItemType(int typeCode) {
        this.typeCode = typeCode;
    }

    public int getTypeCode() {
        return this.typeCode;
    }
}