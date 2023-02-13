package quizzical

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^.*

object Question {
  case class Props(question: String, answers: List[String])

  val component = ScalaComponent.builder[Props]("Question")
    .render_P { props =>
      <.div(
        ^.className := "question-box",
        <.p(^.className := "question", props.question),
        <.div(
          ^.className := "answers",
          <.div(
            ^.className := "answer", props.answers(0),
          ),
          <.div(
            ^.className := "answer", props.answers(1),
          ),
          <.div(
            ^.className := "answer", props.answers(2),
          ),
          <.div(
            ^.className := "answer", props.answers(3),
          )
        )
      )
    }
    .build
}
