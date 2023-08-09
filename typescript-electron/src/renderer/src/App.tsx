import { createSignal } from "solid-js";

export function App() {

  const [email, setEmail] = createSignal('')
  const [password, setPassword] = createSignal('')

  function handleEmailChange(event: Event) {
    // @ts-ignore
    setEmail(event.target?.value)
  }

  function handlePasswordChange(event: Event) {
    // @ts-ignore
    setPassword(event.target?.value)
  }

  async function handleRegisterClick() {
    // @ts-ignore
    const data = window.api.getDatabase()
    if (!email() || !password()) {
      alert('Preencha todos os campos')
      return
    }
    if (email().indexOf('@') === -1) {
      alert('E-mail inválido')
      return
    }
    for (const user of data) {
      if (user.email === email()) {
        alert('Usuário já registrado')
        return
      }
    }
    // @ts-ignore
    window.api.setDatabase({
      email: email(),
      password: password()
    })
    alert('Usuário cadastrado')
    setEmail('')
    setPassword('')
    return
  }

  function handleAuthenticateClick() {
    // @ts-ignore
    const data = window.api.getDatabase()
    let control = false
    for (const user of data) {
      if (user.email === email()) {
        control = true
        if (user.password === password()) {
          alert('Usuário autenticado')
          setEmail('')
          setPassword('')
          return
        }
        alert('Senha incorreta')
        return
      }
    }
    if (!control) {
      alert('Usuário não encontrado')
    }
  }

  return (
    <div class="w-screen h-screen flex items-center justify-center">
      <div class="shadow w-2/4 h-2/4 bg-white rounded-xl flex flex-col items-center justify-center">
        <div>
          <div>
            E-mail
          </div>
          <input
            class="w-full border border-black"
            onInput={handleEmailChange}
            value={email()}
          />
        </div>
        <div class="mt-4">
          <div>
            Senha
          </div>
          <input
            class="w-full border border-black"
            onInput={handlePasswordChange}
            value={password()}
          />
        </div>
        <button class="mt-4 p-2 rounded bg-blue-100" onClick={handleAuthenticateClick}>
          Autenticar
        </button>
        <button class="mt-4 p-2 rounded bg-blue-100" onClick={handleRegisterClick}>
          Registrar
        </button>
      </div>
    </div>
  )
}
