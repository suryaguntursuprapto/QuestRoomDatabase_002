package com.example.pertemuan9.ui.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pertemuan9.data.entity.Mahasiswa
import com.example.pertemuan9.repository.RepositoryMhs
import kotlinx.coroutines.launch

class MahasiswaViewModel(
    private val repositoryMhs: RepositoryMhs
):ViewModel(){
    var uiState by mutableStateOf(MhsUIState())

    //memperbarui state berdasarkan input pengguna
    fun updatestate(mahasiswaEvent: MahasiswaEvent){
        uiState = uiState.copy(
            mahasiswaEvent = mahasiswaEvent,
        )
    }
    //validasi data input pengguna
    private fun validateFields(): Boolean{
        val event = uiState.mahasiswaEvent
        val errorState = FormErrorState(
            nim = if(event.nim.isNotEmpty()) null else "NIM tidak boleh kosong",
            nama = if(event.nim.isNotEmpty()) null else "Nama tidak boleh kosong" ,
            JenisKelamin = if(event.nim.isNotEmpty()) null else "Jenis Kelamin tidak boleh kosong" ,
            alamat = if(event.nim.isNotEmpty()) null else "Alamat tidak boleh kosong" ,
            kelas = if(event.nim.isNotEmpty()) null else "Kelas tidak boleh kosong" ,
            angkatan = if(event.nim.isNotEmpty()) null else "Angkatan tidak boleh kosong" ,
        )
        uiState = uiState.copy(isEntryValid = errorState)
        return errorState.isValid()
    }

    fun saveData(){
        val currentEvent = uiState.mahasiswaEvent

        if (validateFields()){
            viewModelScope.launch {
                try {
                    repositoryMhs.insertMhs(currentEvent.toMahasiswaEntity())
                    uiState = uiState.copy(
                        snackBarMessage = "Data Berhasil Disimpan",
                        mahasiswaEvent = MahasiswaEvent(),
                        isEntryValid = FormErrorState()
                    )
                }catch (e: Exception){
                    uiState = uiState.copy(
                        snackBarMessage = "Data Gagal Disimpan"
                    )
                }
            }
        }else{
            uiState = uiState.copy(
                snackBarMessage = "Input Tidak Valid. Periksa Kembali Data Anda."
            )
        }
    }

    fun resetSnackBarMessage(){
        uiState = uiState.copy(snackBarMessage = null)
    }
}
//data class variabel yang menyimpan
//data input form
data class MahasiswaEvent(
    val nim: String = "",
    val nama: String = "",
    val JenisKelamin: String = "",
    val alamat: String = "",
    val kelas: String = "",
    val angkatan: String = ""
)

fun MahasiswaEvent.toMahasiswaEntity(): Mahasiswa = Mahasiswa(
    nim = nim,
    nama = nama,
    jeniskelamin = JenisKelamin,
    alamat = alamat,
    kelas = kelas,
    angkatan = angkatan
)

data class FormErrorState(
    val nim: String? = null,
    val nama: String? = null,
    val JenisKelamin: String? = null,
    val alamat: String? = null,
    val kelas: String? = null,
    val angkatan: String? = null,
){
    fun isValid():Boolean {
        return nim == null
                && nama == null
                && JenisKelamin == null
                && alamat == null
                && kelas == null
                && angkatan == null
    }
}

data class MhsUIState(
    val mahasiswaEvent: MahasiswaEvent = MahasiswaEvent(),
    val isEntryValid: FormErrorState = FormErrorState(),
    val snackBarMessage: String? = null
)