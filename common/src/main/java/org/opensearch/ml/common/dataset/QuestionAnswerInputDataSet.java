package org.opensearch.ml.common.dataset;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.opensearch.core.common.io.stream.StreamInput;
import org.opensearch.core.common.io.stream.StreamOutput;
import org.opensearch.ml.common.annotation.InputDataSet;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@InputDataSet(MLInputDataType.QUESTION_ANSWER)
public class QuestionAnswerInputDataSet extends MLInputDataset {
    String contextDocs;
    List<String> questionsList;

    @Builder(toBuilder = true)
    public QuestionAnswerInputDataSet(String contextDocs, List<String> questionsList) {
        super(MLInputDataType.QUESTION_ANSWER);
        this.contextDocs = contextDocs;
        this.questionsList = questionsList;
    }

    public QuestionAnswerInputDataSet(StreamInput in) throws IOException {
        super(MLInputDataType.QUESTION_ANSWER);
        this.contextDocs = in.readString();
        int size = in.readInt();
        this.questionsList = new ArrayList<String>(size);
        for (int i = 0; i < size; i++) {
            questionsList.add(i, in.readString());
        }
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        super.writeTo(out);
        out.writeString(this.contextDocs);
        out.writeInt(this.questionsList.size());
        for (String question : this.questionsList) {
            out.writeString(question);
        }
    }
}
