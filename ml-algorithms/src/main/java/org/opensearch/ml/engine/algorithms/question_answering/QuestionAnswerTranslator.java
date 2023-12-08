package org.opensearch.ml.engine.algorithms.question_answering;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.opensearch.ml.common.output.model.ModelTensor;
import org.opensearch.ml.engine.algorithms.text_embedding.SentenceTransformerTextEmbeddingTranslator;

import ai.djl.huggingface.tokenizers.Encoding;
import ai.djl.modality.Input;
import ai.djl.modality.Output;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.DataType;
import ai.djl.translate.Batchifier;
import ai.djl.translate.TranslatorContext;

public class QuestionAnswerTranslator extends SentenceTransformerTextEmbeddingTranslator {
    private String[] tokens;
    private final String QUESTION_ANSWER_NAME = "question_answering";

    @Override
    public NDList processInput(TranslatorContext ctx, Input input) {
        String contextDoc = input.getAsString(0);
        String question = input.getAsString(1);
        NDManager manager = ctx.getNDManager();
        NDList ndList = new NDList();

        Encoding encodings = tokenizer.encode(question, contextDoc);
        this.tokens = encodings.getTokens();
        long[] indices = encodings.getIds();
        long[] attentionMask = encodings.getAttentionMask();
        long[] tokenTypes = encodings.getTypeIds();

        NDArray indicesArray = manager.create(indices);
        indicesArray.setName("input_ids");

        NDArray attentionMaskArray = manager.create(attentionMask);
        attentionMaskArray.setName("attention_mask");

        NDArray tokenTypeArray = manager.create(tokenTypes);
        tokenTypeArray.setName("token_type_ids");

        ndList.add(indicesArray);
        ndList.add(attentionMaskArray);
        ndList.add(tokenTypeArray);
        return ndList;
    }

    @Override
    public Output processOutput(TranslatorContext ctx, NDList list) {
        Output output = new Output(200, "OK");
        List<ModelTensor> outputs = new ArrayList<>();
        Iterator<NDArray> iterator = list.iterator();

        while (iterator.hasNext()) {
            NDArray ndArray = iterator.next();
            String name = QUESTION_ANSWER_NAME;
            Number[] data = ndArray.toArray();
            long[] shape = ndArray.getShape().getShape();
            DataType dataType = ndArray.getDataType();

        }
        return output;
    }

    @Override
    public Batchifier getBatchifier() {
        return Batchifier.STACK;
    }

}
