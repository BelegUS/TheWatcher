package pl.zazakretem.thewatcher;

public class AccelerometerValue extends AbstractValue {
    private float[] accelerometerValues = new float[3];
    private Value x = new Value();
    private Value y = new Value();
    private Value z = new Value();


    public AccelerometerValue setAccelerometerValues(float[] accelerometerValues) {
        this.accelerometerValues = accelerometerValues;
        this.calculate();
        return this;
    }

    public float getGForceX() {
        float nextX = x.getNext();
//        this.db.addMeasure(new MeasureEntity(MeasureEntity.Type.GFORCE_X, nextX));
        return nextX;
    }

    public float getGForceY() {
        float nextY = y.getNext();
//        this.db.addMeasure(new MeasureEntity(MeasureEntity.Type.GFORCE_Y, nextY));
        return nextY;
    }

    public float getGForceZ() {
        float nextZ = z.getNext();
//        this.db.addMeasure(new MeasureEntity(MeasureEntity.Type.GFORCE_Z, nextZ));
        return nextZ;
    }

    public float getMaxGForceX() {
        return x.getMax();
    }

    public float getMaxGForceY() {
        return y.getMax();
    }

    public float getMaxGForceZ() {
        return z.getMax();
    }

    private void calculate() {
        x.add(this.calculateGForce(accelerometerValues[0]));
        y.add(this.calculateGForce(accelerometerValues[1]));
        z.add(this.calculateGForce(accelerometerValues[2]));
    }

    private float calculateGForce(float value) {
        float g = 9.8f;
        return (value / g);
    }

}
