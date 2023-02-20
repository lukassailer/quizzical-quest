package quizzical

import japgolly.scalajs.react.callback.Callback
import japgolly.scalajs.react.vdom.html_<^.*
import org.scalajs.dom
import quizzical.components.{GameOverScreen, LoadingQuestionDisplay, QuestionDisplay, ScoreDisplay}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.util.Random

object Main {
  private val initLives = 5
  private val timeOutAfterAnswer = 5000
  private val root = dom.document.getElementById("app")
  private val url = "https://the-trivia-api.com/api/questions?limit=1"

  def main(args: Array[String]): Unit = {
    renderNextQuestion(GameState(0))
  }

  private def renderNextQuestion(gameState: GameState): Unit = {
    if (gameState.lives == 0) {
      renderGameOver(gameState.score)
    } else {
      fetchQuestion().foreach { questionProps =>
        renderQuestion(questionProps, gameState)
      }
    }
  }

  private def fetchQuestion(): Future[QuestionDisplay.Props] = {
    dom.fetch(url).toFuture.flatMap { response =>
      if (response.ok) {
        response.json().toFuture.map { json =>
          val onlyQuestionObject = json.asInstanceOf[js.Array[js.Dynamic]](0)
          val questionProps = mapJsonToQuestionProps(onlyQuestionObject)

          questionProps
        }
      } else {
        val errorMessage = s"Network Error: ${response.statusText} (${response.status})"
        dom.console.error(errorMessage)
        throw new RuntimeException(errorMessage)
      }
    }.recover { case e: Throwable =>
      val errorMessage = s"Internal Error: ${e.getMessage}"
      dom.console.error(errorMessage)
      throw new RuntimeException(errorMessage)
    }
  }

  private def renderGameOver(score: Int): Unit = {
    GameOverScreen.render(GameOverScreen.Props(score)).renderIntoDOM(root)
  }

  private def renderQuestion(questionProps: QuestionDisplay.Props, gameState: GameState): Unit = {
    def renderNextQuestionWithUpdate(gameState: GameState, update: GameState => GameState): Callback = Callback {
      js.timers.setTimeout(timeOutAfterAnswer) {
        <.div(
          LoadingQuestionDisplay.component(LoadingQuestionDisplay.Props(questionProps.question.text)),
          ScoreDisplay.component(ScoreDisplay.Props(gameState.score, gameState.lives))
        ).renderIntoDOM(root)
        renderNextQuestion(update(gameState))
      }
    }

    val onCorrectAnswer = renderNextQuestionWithUpdate(gameState, _.copy(score = gameState.score + 1))
    val onIncorrectAnswer = renderNextQuestionWithUpdate(gameState, _.copy(lives = gameState.lives - 1))


    val questionPropsWithCallbacks = questionProps.copy(
      onCorrect = onCorrectAnswer,
      onIncorrect = onIncorrectAnswer
    )

    <.div(
      QuestionDisplay.component(questionPropsWithCallbacks),
      ScoreDisplay.component(ScoreDisplay.Props(gameState.score, gameState.lives))
    ).renderIntoDOM(root)
  }

  private def mapJsonToQuestionProps(json: js.Dynamic): QuestionDisplay.Props = {
    val text = json.question.toString
    val correctAnswer = json.correctAnswer.asInstanceOf[String]
    val incorrectAnswers = json.incorrectAnswers.asInstanceOf[js.Array[String]].toList
    val answers = Random.shuffle(
      QuestionDisplay.Answer(correctAnswer, true) :: incorrectAnswers.map(QuestionDisplay.Answer(_, false))
    )

    QuestionDisplay.Props(QuestionDisplay.Question(text, answers))
  }

  case class GameState(score: Int, lives: Int = initLives)
}
