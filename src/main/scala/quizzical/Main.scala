package quizzical

import org.scalajs.dom
import quizzical.components.QuestionDisplay

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.util.Random

object Main {
  def main(args: Array[String]): Unit = {
    val url = "https://the-trivia-api.com/api/questions?limit=1"

    dom.fetch(url).toFuture.flatMap { response =>
      if (response.ok) {
        response.json().toFuture.map { json =>
          val onlyQuestionObject = json.asInstanceOf[js.Array[js.Dynamic]](0)
          val question = mapJsonToQuestionProps(onlyQuestionObject)

          QuestionDisplay.component(question).renderIntoDOM(dom.document.getElementById("app"))
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

  private def mapJsonToQuestionProps(json: js.Dynamic): QuestionDisplay.Props = {
    val text = json.question.toString
    val correctAnswer = json.correctAnswer.asInstanceOf[String]
    val incorrectAnswers = json.incorrectAnswers.asInstanceOf[js.Array[String]].toList
    val answers = Random.shuffle(correctAnswer :: incorrectAnswers)
    val correctAnswerIndex = answers.indexOf(correctAnswer)

    QuestionDisplay.Props(text, answers, correctAnswerIndex)
  }
}
