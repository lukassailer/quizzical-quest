package quizzical

import japgolly.scalajs.react.*
import japgolly.scalajs.react.component.Scala.Component
import japgolly.scalajs.react.vdom.html_<^.*

object QuestionDisplay {
  case class Props(text: String, answers: List[String], correctAnswer: Int)

  case class State(selectedAnswer: Option[Int] = None)

  class Backend($: BackendScope[Props, State]) {
    private def selectAnswer(index: Int): Callback =
      $.modState(_.copy(selectedAnswer = Some(index)))

    def render(props: Props, state: State): VdomElement = {
      <.div(
        ^.className := "question-box",
        <.p(^.className := "question", props.text),
        <.div(
          ^.className := "answers",
          props.answers.zipWithIndex.map { case (answer, index) =>
            <.div(
              ^.className := s"answer ${
                if (state.selectedAnswer.contains(index) && props.correctAnswer == index) "correct"
                else if (state.selectedAnswer.contains(index) && props.correctAnswer != index) "incorrect"
                else ""
              }",
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
