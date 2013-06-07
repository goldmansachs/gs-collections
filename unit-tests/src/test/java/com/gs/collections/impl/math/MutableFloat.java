package com.gs.collections.impl.math;

public final class MutableFloat extends Number implements Comparable<MutableFloat>
{
    private static final long serialVersionUID = 1L;
    private float value = 0.0f;

    public MutableFloat(float value)
    {
        this.value = value;
    }

    public MutableFloat()
    {
        this(0.0f);
    }

    public int compareTo(MutableFloat other)
    {
        return Float.compare(this.value, other.value);
    }

    @Override
    public boolean equals(Object other)
    {
        if (this == other)
        {
            return true;
        }
        if (other == null || this.getClass() != other.getClass())
        {
            return false;
        }
        return Float.compare(((MutableFloat) other).value, this.value) == 0;
    }

    @SuppressWarnings({"UnaryPlus", "NonFinalFieldReferencedInHashCode"})
    @Override
    public int hashCode()
    {
        return this.value == +0.0f ? 0 : Float.floatToIntBits(this.value);
    }

    public void setValue(float value)
    {
        this.value = value;
    }

    public MutableFloat add(float number)
    {
        this.value += number;
        return this;
    }

    public MutableFloat subtract(float number)
    {
        this.value -= number;
        return this;
    }

    public MutableFloat multiply(float number)
    {
        this.value *= number;
        return this;
    }

    public MutableFloat divide(float number)
    {
        this.value /= number;
        return this;
    }

    public MutableFloat min(float number)
    {
        this.value = Math.min(this.value, number);
        return this;
    }

    public MutableFloat max(float number)
    {
        this.value = Math.max(this.value, number);
        return this;
    }

    public MutableFloat abs()
    {
        this.value = Math.abs(this.value);
        return this;
    }

    public Float toFloat()
    {
        return Float.valueOf(this.value);
    }

    @Override
    public int intValue()
    {
        return (int) this.value;
    }

    @Override
    public long longValue()
    {
        return (long) this.value;
    }

    @Override
    public float floatValue()
    {
        return this.value;
    }

    @Override
    public double doubleValue()
    {
        return (double) this.value;
    }

    @Override
    public String toString()
    {
        return "MutableFloat{" +
            "value=" + this.value +
            '}';
    }
}
