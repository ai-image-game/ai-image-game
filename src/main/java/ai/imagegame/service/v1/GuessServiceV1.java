package ai.imagegame.service.v1;

import ai.imagegame.dto.v1.GuessRequestDtoV1;
import ai.imagegame.dto.v1.GuessResultDtoV1;
import ai.imagegame.repository.v1.GameDataEntityV1;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class GuessServiceV1 {
    private Map<String, String> answerCacheMap = new HashMap<>();
    private final static Set<Character> SPECIAL_CHARACTERS = Set.of(' ');

    public void addAnswerCacheMap(GameDataEntityV1 gameDataEntity) {
        this.answerCacheMap.put(gameDataEntity.getUuid(), gameDataEntity.getAnswer());
    }

    public GuessResultDtoV1 guess(GuessRequestDtoV1 request, char guess) {
        String answer = this.getAnswer(request.getImageInfo().getUuid());
        List<Integer> answerIndexList = getAnswerIndexList(answer, guess);
        Set<Character> wrongLetters = request.getGuessInfo().getWrongLetters();
        boolean isCorrect = false;
        if (answerIndexList.isEmpty()) {
            wrongLetters.add(guess);
        } else {
            isCorrect = isCorrectAnswer(answer, guess, request.getQuestionInfo().getMaskedAnswer());
        }
        return new GuessResultDtoV1(isCorrect, answerIndexList, wrongLetters);
    }

    public List<Integer> getAnswerIndexList(String answer, char guess) {
        List<Integer> answerIndexList = new ArrayList<>();
        IntStream.range(0, answer.length())
                .filter(i -> answer.charAt(i) == guess)
                .forEach(answerIndexList::add);
        return answerIndexList;
    }

    public boolean isCorrectAnswer(String answer, char guess, String maskedAnswer) {
        char[] answerLetters = answer.toCharArray();
        Set<Character> answerSet = new HashSet<>();
        for (char c : answerLetters) {
            answerSet.add(c);
        }

        char[] inputLetters = maskedAnswer.replaceAll("\\*", "").toCharArray();
        for (char inputLetter : inputLetters) {
            answerSet.remove(inputLetter);
        }
        answerSet.removeAll(SPECIAL_CHARACTERS);
        answerSet.remove(guess);
        return answerSet.isEmpty();
    }

    public String getAnswer(String id) {
        return this.answerCacheMap.get(id).toLowerCase();
    }
}
