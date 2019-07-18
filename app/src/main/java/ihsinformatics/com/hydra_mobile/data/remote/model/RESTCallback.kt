package ihsinformatics.com.hydra_mobile.data.remote.model

/**
 * @author  Shujaat ali
 * @Email   shujaat.ali@ihsinformatics.com
 * @version 1.0.0
 * @DateCreated   2019-5-14
 */

interface RESTCallback {
     fun onSuccess(o: Any)
     fun onFailure(t: Throwable)
}