package quizzical

import org.scalajs.dom
import quizzical.QuestionDisplay

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js

case class Question(text: String, correctAnswer: String, incorrectAnswers: List[String])

object Main {
  def main(args: Array[String]): Unit = {
    val url = "https://the-trivia-api.com/api/questions?limit=1"

    dom.fetch(url).toFuture.flatMap { response =>
      if (response.ok) {
        response.json().toFuture.map { json =>
          val onlyQuestionObject = json.asInstanceOf[js.Array[js.Dynamic]](0)
          val question = mapJsonToQuestion(onlyQuestionObject)

          QuestionDisplay.component(
            QuestionDisplay.Props(
              question.text,
              question.correctAnswer :: question.incorrectAnswers
            )
          ).renderIntoDOM(dom.document.getElementById("app"))
        }
      } else {
        val errorMessage = s"Network Error: ${response.statusText} (${response.status})"
        dom.console.error(errorMessage)
        throw new RuntimeException(errorMessage)
      }
    }.recover {
      case e: Throwable =>
        val errorMessage = s"Internal Error: ${e.getMessage}"
        dom.console.error(errorMessage)
        throw new RuntimeException(errorMessage)
    }
  }

  private def mapJsonToQuestion(json: js.Dynamic): Question = {
    Question(
      text = json.question.toString,
      correctAnswer = json.correctAnswer.asInstanceOf[String],
      incorrectAnswers = json.incorrectAnswers.asInstanceOf[js.Array[String]].toList
    )
  }
}
