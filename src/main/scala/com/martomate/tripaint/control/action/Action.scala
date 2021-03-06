package com.martomate.tripaint.control.action

import com.martomate.tripaint.model.content.{ImageChangeTrackerImpl, ImageContent}
import com.martomate.tripaint.model.coords.TriImageCoords
import com.martomate.tripaint.model.storage.ImageStorage
import com.martomate.tripaint.model.{SaveLocation, TriPaintModel}
import com.martomate.tripaint.view.TriPaintView

abstract class Action {
  def perform(model: TriPaintModel, view: TriPaintView): Unit

  protected def addImage(model: TriPaintModel, newImage: ImageContent): Unit = {
    if (newImage != null) {
      model.imageGrid(newImage.coords) = newImage
    }
  }

  protected def makeImageContent(model: TriPaintModel, coords: TriImageCoords, storage: ImageStorage): ImageContent = {
    new ImageContent(coords, new ImageChangeTrackerImpl(storage, model.imagePool, model.imageSaver))
  }

  protected def allImages(model: TriPaintModel): Seq[ImageContent] = {
    model.imageGrid.images
  }

  protected def allSelectedImages(model: TriPaintModel): Seq[ImageContent] = {
    model.imageGrid.selectedImages
  }

  protected def save(model: TriPaintModel, view: TriPaintView, images: ImageContent*): Boolean = {
    images.filter(im => !model.imagePool.save(im.storage, model.imageSaver))
      .forall(im => model.imagePool.save(im.storage, model.imageSaver) || saveAs(model, view, im))
  }

  protected def saveAs(model: TriPaintModel, view: TriPaintView, image: ImageContent): Boolean = {
    view.askForSaveFile(image) match {
      case Some(file) =>
        if (model.imagePool.move(image.storage, SaveLocation(file))(view)) {
          val saved = model.imagePool.save(image.storage, model.imageSaver)
          if (!saved) println("Image could not be saved!!")
          saved
        } else false
      case None =>
        false
    }
  }

  protected def saveBeforeClosing(view: TriPaintView, images: ImageContent*): Option[Boolean] = {
    view.askSaveBeforeClosing(images)
  }
}
