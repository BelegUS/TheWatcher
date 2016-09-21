package pl.zazakretem.thewatcher;


public class SpeedValue extends AbstractValue
{
    private float speedValue = 0;
    private Value speed = new Value();

    public SpeedValue setSpeedValue(float speedValue)
    {
        this.speedValue = speedValue;
        this.calculate();
        return this;
    }

    private void calculate()
    {
        this.speed.add(speedValue);
    }

    public float getSpeed()
    {
        float averagedSpeed = speed.getAveraged();
//        this.db.addMeasure(new MeasureEntity(MeasureEntity.Type.SPEED, averagedSpeed));
        return averagedSpeed;
    }

    public float getMaxSpeed()
    {
        return speed.getMax();
    }

}

