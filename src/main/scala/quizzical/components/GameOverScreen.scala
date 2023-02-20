package quizzical.components

import japgolly.scalajs.react.*
import japgolly.scalajs.react.vdom.html_<^.*
import org.scalajs.dom.window

object GameOverScreen {
  val component: ScalaComponent[Props, Unit, Unit, CtorType.Props] =
    ScalaComponent.builder[Props]("GameOverScreen")
      .render_P(render)
      .build

  case class Props(score: Int)

  def render(props: Props): VdomElement = {
    <.div(
      ^.className := "game-over-screen",
      <.h1("Game Over!"),
      <.p(s"Final score: ${props.score}"),
      <.button(
        ^.className := "restart-button",
        ^.onClick --> Callback(window.location.reload()),
        "Restart"
      )
    )
  }
}
