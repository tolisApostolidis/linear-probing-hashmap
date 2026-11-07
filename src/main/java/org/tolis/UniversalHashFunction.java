package org.tolis;

import java.util.Random;

public class UniversalHashFunction {

    private static final int INPUT_BITS = 32;

    private final int outputBits;
    private int[][] hashMatrix;

    public UniversalHashFunction(int capacity) {
        outputBits = findPower(capacity);
        hashMatrix = new int[outputBits][INPUT_BITS];

        generateMatrix();
    }

    private int findPower(int capacity){
        return (int)(Math.log(capacity) / Math.log(2)); // Using math identity: log2(x)=loge(x)/loge(2)
    }

    private void generateMatrix() {
        Random rand = new Random();

        for (int i = 0; i < outputBits; i++) {
            for (int j = 0; j < INPUT_BITS; j++) {
                hashMatrix[i][j] = rand.nextInt(2);
            }
        }
    }

    private int[] generateKeyMatrix(int hashCode) {
        int[] keyMatrix = new int[INPUT_BITS];

        for (int i = 0; i < INPUT_BITS; i++) {
            // Create a bit mask with only the i-th bit set to 1.
            // The loop starts from the least significant bit (LSB).
            int bitMask = 1 << i;

            // Check whether the i-th bit of the hash code is 1 or 0.
            // Store it in reverse position so that keyArray is ordered
            // from the most significant bit (MSB) to the least significant bit (LSB).
            if ((hashCode & bitMask) != 0) {
                keyMatrix[INPUT_BITS - i - 1] = 1;
            } else {
                keyMatrix[INPUT_BITS - i - 1] = 0;
            }

        }

        return keyMatrix;
    }

    public int hash(int hashCode) {
        int[] keyMatrix = generateKeyMatrix(hashCode);
        int[] indexMatrix = new int[outputBits];

        for (int i = 0; i < outputBits; i++) {
            for (int j = 0; j < INPUT_BITS; j++) {
                indexMatrix[i] += keyMatrix[j] * hashMatrix[i][j];
            }

            indexMatrix[i] %= 2;
        }

        int hashIndex = 0;

        for (int i = 0; i < outputBits; i++) {
            if (indexMatrix[i] == 1) {
                hashIndex += (int)Math.pow(2, (outputBits - 1) - i);
            }
        }

        return hashIndex;
    }
}
