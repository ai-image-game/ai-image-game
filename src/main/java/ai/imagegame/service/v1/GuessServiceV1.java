package ai.imagegame.service.v1;

import ai.imagegame.dto.v1.*;
import ai.imagegame.repository.v1.GameDataEntityV1;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
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

    public GuessRequestDtoV1 getGuessInfoByHeader(SimpMessageHeaderAccessor messageHeaderAccessor, GuessInfoDtoV1 guessInfo) {
        if (messageHeaderAccessor.getSessionAttributes() == null) return null;

        GuessRequestDtoV1 requestDto = new GuessRequestDtoV1();
        requestDto.setGuessInfo(guessInfo);
        requestDto.setImageInfo((ImageInfoDtoV1) messageHeaderAccessor.getSessionAttributes().get("imageInfo"));
        requestDto.setGameInfo((GameInfoDtoV1) messageHeaderAccessor.getSessionAttributes().get("gameInfo"));
        requestDto.setQuestionInfo((QuestionInfoDtoV1) messageHeaderAccessor.getSessionAttributes().get("questionInfo"));
        return requestDto;
    }

    public void addGuessInfoToHeader(SimpMessageHeaderAccessor messageHeaderAccessor, GuessResultDtoV1 response) {
        if (messageHeaderAccessor.getSessionAttributes() != null) {
            messageHeaderAccessor.getSessionAttributes().put("guessInfo", response);
        }
    }

    public GuessResultDtoV1 guess(GuessRequestDtoV1 request) {
        String answer = this.getAnswer(request.getImageInfo().getUuid());
        char input = request.getGuessInfo().getInput();
        List<Integer> answerIndexList = getAnswerIndexList(answer, input);
        Set<Character> wrongLetters = request.getGuessInfo().getWrongLetters();
        boolean isCorrect = false;
        if (answerIndexList.isEmpty()) {
            wrongLetters.add(input);
        } else {
            isCorrect = isCorrectAnswer(answer, input, request.getQuestionInfo().getMaskedAnswer());
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
