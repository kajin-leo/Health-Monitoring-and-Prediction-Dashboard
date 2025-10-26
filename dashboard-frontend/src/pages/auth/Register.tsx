import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { authClient } from '../../service/axios';
import { Alert, Button, Input } from '@heroui/react';
import { CircleX } from 'lucide-react';
import Logo from '../../assets/豹豹Logo.svg';
import LogoDark from '../../assets/豹豹Logo-Dark.svg';
import JsonUploadField from '../../components/FileUpload/JsonUploadField';

const Register = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [errorResponse, setErrorResponse] = useState('');
  const [isUsernameInvalid, setUsernameInvalid] = useState(false);
  const [isPasswordInvalid, setPasswordInvalid] = useState(false);
  const [isConfirmInvalid, setConfirmInvalid] = useState(false);
  const [file, setFile] = useState<File | null>(null)
  const [fileError, setFileError] = useState<string>("");

  const navigate = useNavigate();

  const handleRegister = async () => {
    setIsLoading(true);
    setUsernameInvalid(false);
    setPasswordInvalid(false);
    setConfirmInvalid(false);
    setErrorResponse('');

    if (username.trim() === '' || username.length < 3) {
      setUsernameInvalid(true);
      setIsLoading(false);
      return;
    }

    if (password.trim() === '' || password.length < 6) {
      setPasswordInvalid(true);
      setIsLoading(false);
      return;
    }

    if (password !== confirmPassword) {
      setConfirmInvalid(true);
      setIsLoading(false);
      return;
    }

    if (!file) {
      setFileError("Please upload exercise data.");
      setIsLoading(false);
      return
    }

    setFileError("");
    try {
      const form = new FormData()
      form.append('username', username)
      form.append('password', password)
      form.append('exerciseJson', file)
      const { data } = await authClient.post('/auth/register', form, {
        headers: { 'Content-Type': 'multipart/form-data' }
      });
      // const { data } = await authClient.post('/auth/register', { username, password });
      console.log('Register success:', data);
      navigate('/login');
    } catch (error) {
      console.error(error);
      setErrorResponse(
        error.code === 'ERR_NETWORK'
          ? error.message
          : error.response?.data?.error || error.response?.data?.errors?.[0] || 'Registration failed'
      );
    } finally {
      setIsLoading(false);
    }
  };

  const lightColorScheme = "bg-white/80 outline-white/20"
  const darkColorScheme = "dark:bg-black/50 dark:outline-gray-100/20"

  return (
    <form
      onSubmit={(e) => {
        e.preventDefault();
        handleRegister();
      }}
    >
      <div className={`${lightColorScheme} ${darkColorScheme} backdrop-blur-xl outline-1 flex flex-col items-center gap-4 rounded-3xl p-4 min-w-80 shadow-xl select-none`}>
        <div className="flex flex-col items-center my-2">
          <img src={Logo} className='w-15 block dark:hidden' />
          <img src={LogoDark} className='w-15 hidden dark:block' />
          <h1 className="text-2xl font-semibold">Register</h1>
        </div>

        <Input
          label="Username"
          placeholder="Please enter your username"
          value={username}
          onValueChange={setUsername}
          errorMessage="Username must be at least 3 characters."
          isInvalid={isUsernameInvalid}
        />

        <Input
          label="Password"
          placeholder="Please enter your password"
          type="password"
          value={password}
          onValueChange={setPassword}
          errorMessage="Password must be at least 6 characters."
          isInvalid={isPasswordInvalid}
        />

        <Input
          label="Confirm Password"
          placeholder="Please confirm your password"
          type="password"
          value={confirmPassword}
          onValueChange={setConfirmPassword}
          errorMessage="Passwords do not match."
          isInvalid={isConfirmInvalid}
        />

        <JsonUploadField
          label="Exercise Data"
          placeholder="Drag a file here, or choose a file to upload."
          helperText={!fileError ? "File permitted: JSON" : undefined}
          errorText={fileError || undefined}
          onFileSelected={(f) => {
            setFile(f);
            if (f) setFileError("");
          }}
          required
        />

        <Button type="submit" color="primary" isLoading={isLoading} onPress={handleRegister} className="mt-4">
          Register
        </Button>

        {errorResponse !== '' ? (
          <Alert icon={<CircleX stroke="white" />} color="danger">
            {errorResponse}
          </Alert>
        ) : (
          ''
        )}
      </div>
    </form>
  );
};

export default Register;
