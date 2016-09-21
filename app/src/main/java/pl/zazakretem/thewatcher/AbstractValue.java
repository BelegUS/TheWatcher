package pl.zazakretem.thewatcher;

import java.util.ArrayDeque;

abstract class AbstractValue
{
    protected class Value {

        private final int LENGTH = 10;

        private float maxValue = 0;
        private ArrayDeque<Float> queue = new ArrayDeque<Float>();

        public void add(float value){
            float absoluteValue = Math.abs(value);
            if(absoluteValue > maxValue) {
                maxValue = absoluteValue;
            }
            queue.add(value);
            if(queue.size() > LENGTH){
                queue.poll();
            }
        }

        public float getAveraged()
        {
            int size = queue.size();
            float sum = 0;
            for (float element : queue) {
                sum += element;
            }
            return sum / size;
        }

        public float getMax()
        {
            return this.maxValue;
        }

        public float getNext()
        {
            return queue.poll();
        }
    }
}
