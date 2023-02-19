package quizzical.components

import japgolly.scalajs.react.*
import japgolly.scalajs.react.component.Scala.Component
import japgolly.scalajs.react.vdom.html_<^.*

object QuestionDisplay {
  val component: Component[Props, State, Backend, CtorType.Props] = ScalaComponent.builder[Props]("Question")
    .initialState(State())
    .renderBackend[Backend]
    .build

  case class Props(
                    text: String,
                    answers: List[String],
                    correctAnswer: Int,
                    onCorrect: Callback,
                    onIncorrect: Callback
                  )

  case class State(selectedAnswer: Option[Int] = None)

  class Backend($: BackendScope[Props, State]) {
    def render(props: Props, state: State): VdomElement = {
      <.div(
        ^.className := "question-box",
        <.p(^.className := "question", props.text),
        <.div(
          ^.className := "answers",
          props.answers.zipWithIndex.map { case (answer, index) =>
            val mod = ^.className := answerClassName(index, state.selectedAnswer, props.correctAnswer)
            <.div(
              ^.key := index,
              mod,
              ^.onClick --> selectAnswer(index),
              answer
            )
          }.toTagMod
        )
      )
    }

    private def selectAnswer(index: Int): Callback =
      $.setState(State(Some(index))).flatMap { _ =>
        $.props.flatMap { props =>
          if (index == props.correctAnswer) props.onCorrect else props.onIncorrect
        }
      }

    private def answerClassName(index: Int, selectedAnswer: Option[Int], correctAnswer: Int): String =
      s"answer ${
        selectedAnswer match {
          case Some(selected) if index == correctAnswer => "correct"
          case Some(selected) if index == selected => "incorrect"
          case Some(_) => "disabled"
          case _ => ""
        }
      }"
  }
}
