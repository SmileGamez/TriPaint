package com.martomate.tripaint

import com.martomate.tripaint.control.TriPaintController
import com.martomate.tripaint.model.TriPaintModel
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage

object TriPaint extends JFXApp {
  val model: TriPaintModel = new TriPaintModel
  val controller = new TriPaintController(model)
  stage = controller.view.asInstanceOf[PrimaryStage]
}
