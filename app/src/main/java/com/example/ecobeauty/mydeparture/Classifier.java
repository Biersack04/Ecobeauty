package com.example.ecobeauty.mydeparture;

import android.graphics.Bitmap;

import com.example.ecobeauty.main.Constants;

import org.pytorch.IValue;
import org.pytorch.Module;
import org.pytorch.Tensor;
import org.pytorch.torchvision.TensorImageUtils;

public class Classifier {
    private Module model;
    private float[] mean = {0.485f, 0.456f, 0.406f};
    private float[] std = {0.229f, 0.224f, 0.225f};
    private int maxIndex, classIndex;
    private float maxvalue;
    private Tensor tensor, outputs;
    private IValue inputs;
    private float[] scores;

    public Classifier(String modelPath) {
        model = Module.load(modelPath);
    }

    public Tensor preprocess(Bitmap bitmap, int size) {
        bitmap = Bitmap.createScaledBitmap(bitmap, size, size, false);
        return TensorImageUtils.bitmapToFloat32Tensor(bitmap, this.mean, this.std);
    }

    public int argMax(float[] inputs) {
        maxIndex = -1;
        maxvalue = 0.0f;
        for (int i = 0; i < inputs.length; i++) {
            if (inputs[i] > maxvalue) {
                maxIndex = i;
                maxvalue = inputs[i];
            }
        }
        return maxIndex;
    }

    public String predict(Bitmap bitmap) {
        tensor = preprocess(bitmap, Constants.IMG_SIZE);
        inputs = IValue.from(tensor);
        outputs = model.forward(inputs).toTensor();
        scores = outputs.getDataAsFloatArray();
        classIndex = argMax(scores);
        return Constants.IMAGENET_CLASSES[classIndex];
    }
}