package org.opensearch.ml.common.input.nlp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.opensearch.core.common.io.stream.StreamInput;
import org.opensearch.core.common.io.stream.StreamOutput;
import org.opensearch.core.xcontent.XContentBuilder;
import org.opensearch.core.xcontent.XContentParser;
import org.opensearch.ml.common.FunctionName;
import org.opensearch.ml.common.dataset.MLInputDataset;
import org.opensearch.ml.common.dataset.QuestionAnswerInputDataSet;
import org.opensearch.ml.common.input.MLInput;
import static org.opensearch.core.xcontent.XContentParserUtils.ensureExpectedToken;


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

     @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        builder.field(ALGORITHM_FIELD, algorithm.name());
        if(parameters != null) {
            builder.field(ML_PARAMETERS_FIELD, parameters);
        }
        if(inputDataset != null) {
            QuestionAnswerInputDataSet ds = (QuestionAnswerInputDataSet) this.inputDataset;
            List<String> questionsList = ds.getQuestionsList();
            String contextDoc = ds.getContextDocs();
            builder.field(CONTEXT_DOCS, contextDoc);
            if (questionsList != null && !questionsList.isEmpty()) {
                builder.startArray(QUESTIONS_LIST);
                for(String d : questionsList) {
                    builder.value(d);
                }
                builder.endArray();
            }
        }
        builder.endObject();
        return builder;
    }

     public QuestionAnswerMLInput(XContentParser parser, FunctionName functionName) throws IOException {
        super();
        this.algorithm = functionName;
        List<String> questionsList = new ArrayList<>();
        String contextDoc = null;

        ensureExpectedToken(XContentParser.Token.START_OBJECT, parser.currentToken(), parser);
        while (parser.nextToken() != XContentParser.Token.END_OBJECT) {
            String fieldName = parser.currentName();
            parser.nextToken();

            switch (fieldName) {
                case QUESTIONS_LIST:
                    ensureExpectedToken(XContentParser.Token.START_ARRAY, parser.currentToken(), parser);
                    while (parser.nextToken() != XContentParser.Token.END_ARRAY) {
                        String context = parser.text();
                        questionsList.add(context);
                    }
                    break;
                case CONTEXT_DOCS: 
                    contextDoc = parser.text();
                default:
                    parser.skipChildren();
                    break;
            }
        }        
        if(questionsList.isEmpty()) {
            throw new IllegalArgumentException("No question list");
        }
        if(contextDoc == null) {
            throw new IllegalArgumentException("No context documents");
        }
        inputDataset = new QuestionAnswerInputDataSet(contextDoc, questionsList);
    }
}
