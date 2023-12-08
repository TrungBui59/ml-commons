package org.opensearch.ml.engine.algorithms.question_answering;

import java.util.ArrayList;
import java.util.List;

import org.opensearch.ml.common.dataset.MLInputDataset;
import org.opensearch.ml.common.dataset.QuestionAnswerInputDataSet;
import org.opensearch.ml.common.input.MLInput;
import org.opensearch.ml.common.model.MLModelConfig;
import org.opensearch.ml.common.output.model.ModelTensorOutput;
import org.opensearch.ml.common.output.model.ModelTensors;
import org.opensearch.ml.engine.algorithms.DLModel;

import ai.djl.modality.Input;
import ai.djl.modality.Output;
import ai.djl.translate.TranslateException;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorFactory;

public class QuestionAnswerModel extends DLModel {

    @Override
    public ModelTensorOutput predict(String modelId, MLInput mlInput) throws TranslateException {
        MLInputDataset inputDataSet = mlInput.getInputDataset();
        List<ModelTensors> tensorOutputs = new ArrayList<>();
        Output output;
        QuestionAnswerInputDataSet ds = (QuestionAnswerInputDataSet) inputDataSet;
        String context = ds.getContextDocs();
        for (String question : ds.getQuestionsList()) {
            Input input = new Input();
            input.add(context);
            input.add(question);
            output = getPredictor().predict(input);
            ModelTensors outputTensors = ModelTensors.fromBytes(output.getData().getAsBytes());
            tensorOutputs.add(outputTensors);
        }
        return new ModelTensorOutput(tensorOutputs);
    }

    @Override
    public Translator<Input, Output> getTranslator(String engine, MLModelConfig modelConfig) {
        return new QuestionAnswerTranslator();
    }

    @Override
    public TranslatorFactory getTranslatorFactory(String engine, MLModelConfig modelConfig) {
        return null;
    }

}
