package org.opensearch.ml.common.input.nlp;

import java.io.IOException;

import org.opensearch.core.common.io.stream.StreamInput;
import org.opensearch.core.common.io.stream.StreamOutput;
import org.opensearch.ml.common.FunctionName;
import org.opensearch.ml.common.dataset.MLInputDataset;
import org.opensearch.ml.common.input.MLInput;

@org.opensearch.ml.common.annotation.MLInput(functionNames=FunctionName.QUESTION_ANSWER)
public class QuestionAnswerMLInput extends MLInput {
    public QuestionAnswerMLInput(FunctionName algorithm, MLInputDataset inputDataset) {
        super(algorithm, null, inputDataset);
    }

    public QuestionAnswerMLInput(StreamInput input) throws IOException {
        super(input);
    }

    @Override
    public void writeTo(StreamOutput output) throws IOException {
        super.writeTo(output);
    }
}
