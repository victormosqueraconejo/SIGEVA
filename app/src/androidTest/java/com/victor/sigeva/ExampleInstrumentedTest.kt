package com.victor.sigeva

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Before
import androidx.test.runner.AndroidJUnit4
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiObject2
import androidx.test.uiautomator.Until
import junit.framework.TestCase.assertTrue
import org.hamcrest.core.IsNull.notNullValue
import org.junit.Test
import org.junit.runner.RunWith


private const val BASIC_SAMPLE_PACKAGE = "com.victor.sigeva"
private const val LAUNCH_TIMEOUT = 5000L
private const val STRING_TO_BE_TYPED = "UiAutomator"

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 18)
class ChangeTextBehaviorTest2 {

    private lateinit var device: UiDevice

    @Before
    fun startMainActivityFromHomeScreen() {
        // Initialize UiDevice instance
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

        // Start from the home screen
        device.pressHome()

        // Wait for launcher
        val launcherPackage: String = device.launcherPackageName
        assertThat(launcherPackage, notNullValue())
        device.wait(
            Until.hasObject(By.pkg(launcherPackage).depth(0)),
            LAUNCH_TIMEOUT
        )

        // Launch the app
        val context = ApplicationProvider.getApplicationContext<Context>()
        val intent = context.packageManager.getLaunchIntentForPackage(
            BASIC_SAMPLE_PACKAGE).apply {
            // Clear out any previous instances
            this!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        context.startActivity(intent)

        // Wait for the app to appear
        device.wait(
            Until.hasObject(By.pkg(BASIC_SAMPLE_PACKAGE).depth(0)),
            LAUNCH_TIMEOUT
        )
    }


    fun id(id : String) : String {
        return "com.victor.sigeva:id/$id"
    }


    fun Find(id: String) : UiObject2
    {
        return device.wait(Until.findObject(By.res(id(id))), 5000) ?: throw AssertionError("" +
                "No se encontro: $id")
    }

    @Test
    fun LoginEnLaAppConUsuarioYaRegistrado() {
        var inputCorreo = Find("input_email")
        var inputPassword = Find("input_password")

        inputCorreo.text = "victormanuelmosqueraconejo@gmail.com"
        inputPassword.text = "123456"


        Find("btn_login").click()


        assertTrue("No encontrado recycler", device.wait(Until.hasObject(By.res(id("recyclerViewVotacionesActivas"))),5000) != null)


    }

    @Test
    fun AccederACandidatosDeLaPrimeraEleccion() {
        LoginEnLaAppConUsuarioYaRegistrado()
        Thread.sleep(5000)

        var recycler = Find("recyclerViewVotacionesActivas")

        var item = recycler.children.first()
        var botonEleccion = item.findObject(By.res(id("btnParctiparVotaciones")))
        botonEleccion.click()

    }

    @Test
    fun VotarPorElPrimerCandidato() {
        AccederACandidatosDeLaPrimeraEleccion()



    }





}