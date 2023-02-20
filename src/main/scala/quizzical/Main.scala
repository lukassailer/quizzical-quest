package quizzical

import japgolly.scalajs.react.callback.Callback
import japgolly.scalajs.react.vdom.html_<^.*
import org.scalajs.dom
import quizzical.components.{GameOverScreen, LoadingQuestionDisplay, QuestionDisplay, ScoreDisplay}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.util.Random

object Main {
  def main(args: Array[String]): Unit = {
    val initLives = 5
    val root = dom.document.getElementById("app")
    val url = "https://the-trivia-api.com/api/questions?limit=1"

    renderNextQuestion(0)

    def renderNextQuestion(score: Int, lives: Int = initLives): Unit = {
      if (lives == 0) {
        renderGameOver(score)
      } else {
        dom.fetch(url).toFuture.flatMap { response =>
          if (response.ok) {
            response.json().toFuture.map { json =>
              val onlyQuestionObject = json.asInstanceOf[js.Array[js.Dynamic]](0)
              val questionProps = mapJsonToQuestionProps(onlyQuestionObject)

              renderQuestion(questionProps, score, lives)
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
    }

    def renderGameOver(score: Int): Unit = {
      GameOverScreen.render(GameOverScreen.Props(score)).renderIntoDOM(dom.document.getElementById("app"))
    }

    def renderQuestion(questionProps: QuestionDisplay.Props, score: Int, lives: Int): Unit = {
      val onCorrectAnswer = Callback {
        js.timers.setTimeout(5000) {
          <.div(
            LoadingQuestionDisplay.component(LoadingQuestionDisplay.Props(questionProps.text)),
            ScoreDisplay.component(ScoreDisplay.Props(score, lives))
          ).renderIntoDOM(root)
          renderNextQuestion(score + 1, lives)
        }
      }
      val onIncorrectAnswer = Callback {
        js.timers.setTimeout(5000) {
          <.div(
            LoadingQuestionDisplay.component(LoadingQuestionDisplay.Props(questionProps.text)),
            ScoreDisplay.component(ScoreDisplay.Props(score, lives))
          ).renderIntoDOM(root)
          renderNextQuestion(score, lives - 1)
        }
      }

      val questionPropsWithCallbacks = questionProps.copy(
        onCorrect = onCorrectAnswer,
        onIncorrect = onIncorrectAnswer
      )

      <.div(
        QuestionDisplay.component(questionPropsWithCallbacks),
        ScoreDisplay.component(ScoreDisplay.Props(score, lives))
      ).renderIntoDOM(root)
    }
  }

  private def mapJsonToQuestionProps(json: js.Dynamic): QuestionDisplay.Props = {
    val text = json.question.toString
    val correctAnswer = json.correctAnswer.asInstanceOf[String]
    val incorrectAnswers = json.incorrectAnswers.asInstanceOf[js.Array[String]].toList
    val answers = Random.shuffle(correctAnswer :: incorrectAnswers)
    val correctAnswerIndex = answers.indexOf(correctAnswer)

    QuestionDisplay.Props(text, answers, correctAnswerIndex)
  }
}
