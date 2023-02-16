package quizzical.components

import japgolly.scalajs.react.*
import japgolly.scalajs.react.component.Scala.Component
import japgolly.scalajs.react.vdom.html_<^.*

object QuestionDisplay {
  val component: Component[Props, State, Backend, CtorType.Props] = ScalaComponent.builder[Props]("Question")
    .initialState(State())
    .renderBackend[Backend]
    .build

  case class Props(text: String, answers: List[String], correctAnswer: Int)

  case class State(selectedAnswer: Option[Int] = None)

  class Backend($: BackendScope[Props, State]) {
    def render(props: Props, state: State): VdomElement = {
      <.div(
        ^.className := "question-box",
        <.p(^.className := "question", props.text),
        <.div(
          ^.className := "answers",
          props.answers.zipWithIndex.map { case (answer, index) =>
            <.div(
              ^.key := index,
              ^.className := answerClassName(index, state.selectedAnswer, props.correctAnswer),
              ^.onClick --> selectAnswer(index),
              answer
            )
          }.toTagMod
        )
      )
    }

    private def selectAnswer(index: Int): Callback =
      $.setState(State(Some(index)))

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
