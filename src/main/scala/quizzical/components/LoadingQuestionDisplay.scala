package quizzical.components

import japgolly.scalajs.react.*
import japgolly.scalajs.react.component.Scala.Component
import japgolly.scalajs.react.vdom.VdomElement

object LoadingQuestionDisplay {
  val component: Component[Props, Unit, Unit, CtorType.Props] = ScalaComponent.builder[Props]("LoadingQuestion")
    .render_P(render)
    .build

  case class Props(text: String)
  
  def render(props: Props): VdomElement =
    QuestionDisplay.component(
      QuestionDisplay.Props(props.text, List("", "", "", ""), -1)
    )
}
