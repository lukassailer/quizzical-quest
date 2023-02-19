package quizzical.components

import japgolly.scalajs.react.*
import japgolly.scalajs.react.vdom.html_<^.*

object ScoreDisplay {
  val component: ScalaComponent[Props, Unit, Unit, CtorType.Props] = ScalaComponent.builder[Props]("ScoreDisplay")
    .render_P(render)
    .build

  case class Props(score: Int, lives: Int)

  def render(props: Props): VdomElement = {
    <.div(
      ^.className := "score-display",
      <.div(
        ^.className := "lives",
        (1 to props.lives).map { i =>
          <.p(^.className := "red", "❤")
        }.toTagMod
      ),
      <.div(
        ^.className := "score",
        s"Score: ${props.score}"
      )
    )
  }
}
