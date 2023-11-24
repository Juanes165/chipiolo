package com.aragang.chipiolo.ProfilePicUpdate

import android.Manifest
import android.net.Uri
import android.util.Log
import android.view.ViewGroup
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.util.concurrent.Executor

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(
) {
    // Permisos de la camara
    val permissionState = rememberPermissionState(Manifest.permission.CAMERA)

    // El contexto es el composable en el que la camara esta metida
    val context = LocalContext.current
    val cameraController = remember {
        LifecycleCameraController(context)
    }
    // Ciclo de vida para que la camara se cierre si no esta el composable contexto
    val lifecycle = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
        permissionState.launchPermissionRequest()
    }

    // Dialogo con la preview de la foto
    var showProfilePictureDialog by remember { mutableStateOf(false) }

    // Direccion Uri de la foto
    var photoUri by remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Green),

        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier
                    .size(80.dp)
                    .border(8.dp, Color.White, CircleShape),
                shape = CircleShape,
                onClick = {
                    val executor = ContextCompat.getMainExecutor(context)
                    takePicture(
                        cameraController,
                        executor,
                        setPhotoUri = { photoUri = it },
                        showDialog = { showProfilePictureDialog = true })
                },
                containerColor = Color(255, 255, 255, 50),
                elevation = FloatingActionButtonDefaults.elevation(0.dp),
            ) {}
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            ) {
                Text(
                    text = "Toca el boton para tomar una foto",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .shadow(10.dp, CircleShape),
                    color = Color(230, 230, 230, 255),
                    fontSize = 15.sp,
                )
            }
        },

        floatingActionButtonPosition = FabPosition.Center,
    ) {
        Box {
            if (permissionState.status.isGranted) {
                CameraComposable(cameraController, lifecycle, modifier = Modifier.padding(it))
            }
        }
    }

    // DIALOGO CON LA PREVIEW DE LA FOTO
    if (showProfilePictureDialog) {
        Dialog(onDismissRequest = { showProfilePictureDialog = false }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .border(
                        BorderStroke(1.dp, Color(0, 0, 0, 255)),
                        RoundedCornerShape(16.dp)
                    )
            ) {
                Column {
                    Text(
                        text = "Tu foto",
                        fontSize = 24.sp,
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.CenterHorizontally),
                        textAlign = TextAlign.Center
                    )

                    AsyncImage(
                        model = photoUri,
                        contentDescription = "Profile picture",
                        modifier = Modifier
                            .size(200.dp)
                            .clip(CircleShape)
                            .border(
                                BorderStroke(4.dp, Color.White),
                                CircleShape
                            )
                            .padding(4.dp)
                            .clip(CircleShape)
                            .align(Alignment.CenterHorizontally),
                        contentScale = ContentScale.Crop
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = { showProfilePictureDialog = false },
                            modifier = Modifier
                                .padding(top = 16.dp, end = 5.dp)
                                .width(115.dp)
                        ) {
                            androidx.compose.material3.Text("Repetir", fontSize = 14.sp)
                        }

                        Button(
                            onClick = {
                                uploadProfilePicture(Uri.parse(photoUri))
                            },
                            modifier = Modifier
                                .padding(top = 16.dp, start = 5.dp)
                                .width(115.dp)
                        ) {
                            androidx.compose.material3.Text("Aceptar", fontSize = 14.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CameraComposable(
    cameraController: LifecycleCameraController,
    lifecycle: LifecycleOwner,
    modifier: Modifier = Modifier,
) {

    cameraController.bindToLifecycle(lifecycle)
    AndroidView(modifier = Modifier, factory = { context ->
        val previewView = PreviewView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
        previewView.controller = cameraController

        previewView
    })
}

private fun takePicture(
    cameraController: LifecycleCameraController,
    executor: Executor,
    setPhotoUri: (String) -> Unit,
    showDialog: () -> Unit = {}
) {
    val file = File.createTempFile("profileimage", ".jpg")
    val outputDirectory = ImageCapture.OutputFileOptions.Builder(file).build()
    cameraController.takePicture(
        outputDirectory,
        executor,
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                setPhotoUri(outputFileResults.savedUri.toString())
                showDialog()
            }

            override fun onError(exception: ImageCaptureException) {
                println()
            }

        })
}

fun uploadProfilePicture(uri: Uri) {
    Log.d("TAG", "PHOTOURI: ${uri.lastPathSegment}")
    val storageRef = FirebaseStorage.getInstance().getReference()
    val fileRef = storageRef.child("${uri.lastPathSegment}")
    val uploadTask = fileRef.putFile(uri)

    uploadTask.addOnSuccessListener {
//        Toast.makeText(this, "Image uploaded successfully", Toast.LENGTH_SHORT)
//            .show()
        Log.d("TAG", "uploadProfilePicture: ${it.metadata?.path}")
    }.addOnFailureListener {
//        Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show()
        Log.d("TAG", "uploadProfilePicture: ${it.message}")
    }
}