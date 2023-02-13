package quizzical

import japgolly.scalajs.react.*
import japgolly.scalajs.react.component.Scala.Component
import japgolly.scalajs.react.vdom.html_<^.*

object Question {
  case class Props(question: String, answers: List[String])

  case class State(selectedAnswer: Option[Int] = None)

  class Backend($: BackendScope[Props, State]) {
    private def selectAnswer(index: Int): Callback =
      $.modState(_.copy(selectedAnswer = Some(index)))

    def render(props: Props, state: State): VdomElement = {
      <.div(
        ^.className := "question-box",
        <.p(^.className := "question", props.question),
        <.div(
          ^.className := "answers",
          props.answers.zipWithIndex.map { case (answer, index) =>
            <.div(
              ^.className := s"answer ${if (state.selectedAnswer.contains(index)) "selected" else ""}",
              ^.onClick --> selectAnswer(index),
              answer
            )
          }.toTagMod
        )
      )
    }
  }

  val component: Component[Props, State, Backend, CtorType.Props] = ScalaComponent.builder[Props]("Question")
    .initialState(State())
    .renderBackend[Backend]
    .build
}
