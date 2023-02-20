package quizzical.components

import japgolly.scalajs.react.*
import japgolly.scalajs.react.component.Scala.Component
import japgolly.scalajs.react.vdom.html_<^.*

object QuestionDisplay {
  val component: Component[Props, State, Backend, CtorType.Props] =
    ScalaComponent.builder[Props]("Question")
      .initialState(State())
      .renderBackend[Backend]
      .build

  case class Question(text: String, answers: List[Answer])

  case class Answer(text: String, isCorrect: Boolean)

  case class Props(
                    question: Question,
                    onCorrect: Callback = Callback.empty,
                    onIncorrect: Callback = Callback.empty
                  )

  case class State(selectedAnswer: Option[Int] = None)

  class Backend($: BackendScope[Props, State]) {
    def render(props: Props, state: State): VdomElement = {
      <.div(
        ^.className := "question-box",
        <.p(^.className := "question", props.question.text),
        <.div(
          ^.className := "answers",
          props.question.answers.zipWithIndex.map { case (answer, index) =>
            <.div(
              ^.key := index,
              ^.className := answerClassName(index, state.selectedAnswer, answer),
              ^.onClick --> selectAnswer(index),
              answer.text
            )
          }.toTagMod
        )
      )
    }

    private def selectAnswer(index: Int): Callback =
      $.setState(State(Some(index))).flatMap { _ =>
        $.props.flatMap { props =>
          val answer = props.question.answers(index)
          if (answer.isCorrect) props.onCorrect else props.onIncorrect
        }
      }

    private def answerClassName(index: Int, selectedAnswer: Option[Int], answer: Answer): String =
      s"answer ${
        selectedAnswer match {
          case Some(selected) if answer.isCorrect => "correct"
          case Some(selected) if index == selected => "incorrect"
          case Some(_) => "disabled"
          case _ => ""
        }
      }"
  }
}
