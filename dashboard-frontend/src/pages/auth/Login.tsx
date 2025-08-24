import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { authClient } from '../../service/axios';
import { Alert, Button, Input } from '@heroui/react';
import { CircleX, XCircle } from 'lucide-react';

const Login = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    const [errorResponse, setErrorResponse] = useState('');
    const navigate = useNavigate();
    
    const handleLogin = async () => {
        setIsLoading(true);
        try {
            const { data } = await authClient.post('/auth/login', {username, password});
            localStorage.setItem('token', data.accessToken);
            localStorage.setItem('role', data.role);
            if(data.role === 'USER') {
                navigate('/')
            } else {
                navigate('/ops')
            }
        } catch (error) {
            console.error(error);
            setErrorResponse(error.code === 'ERR_NETWORK' ? error.message : (error.response.data.error || error.response.data.errors[0]));
        } finally {
            setIsLoading(false);
        }
    }
    return (
        <div className="bg-white/80 backdrop-blur-xl outline-1 outline-white/20 flex flex-col items-center gap-4 rounded-3xl p-4 min-w-80 shadow-xl">
            <h1 className='text-xl font-semibold m-2'>Login</h1>
            <Input label='Username' placeholder='Please enter your username' value={username} onValueChange={setUsername}/>
            <Input label='Password' placeholder='Please enter your password' type='password' value={password} onValueChange={setPassword}/>
            <Button color='primary' isLoading={isLoading} onPress={handleLogin} className='mt-4'>
                Sign In
            </Button>
            {errorResponse != '' ? <Alert icon={<CircleX stroke='white'/>} color='danger'>{errorResponse}</Alert> : ''}
        </div>
    )
}

export default Login;