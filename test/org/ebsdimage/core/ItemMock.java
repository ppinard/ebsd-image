package org.ebsdimage.core;

public class ItemMock {

    public final String name;



    public ItemMock(String name) {
        this.name = name;
    }



    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        ItemMock other = (ItemMock) obj;
        if (!name.equals(other.name))
            return false;

        return true;
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + name.hashCode();
        return result;
    }



    @Override
    public String toString() {
        return name;
    }

}