import { useState, useEffect, useRef } from 'react'
import { Routes, Route, useNavigate } from 'react-router-dom'
import ProtectedRoute from './config/ProtectedRoute'
import SideBar from './components/Sidebar'
import ClientHome from './pages/client/Home'
import ChartDemo from './pages/client/ChartDemo'
import OpsHome from './pages/ops/Home'
import Login from './pages/auth/Login'
import Unauthorized from './pages/auth/Unauthorized'
import { UserProvider } from './context/UserContext'
import { useUser } from './context/UserContext'
import { CircularProgress } from '@heroui/react'

function ClientLayout({ children }) {
    const { loading, error } = useUser();

    if (loading) {
        return (
            <div className="fixed w-full h-full bg-gradient-to-br from-sky-50 to-blue-100 flex items-center py-2">
                <div className='w-full h-full bg-white/20 backdrop-blur-3xl flex items-center justify-center'>
                    <div>
                        <CircularProgress label="Loading..." />
                    </div>
                </div>
            </div>
        )
    }

    return (
        <div className="fixed w-full h-full bg-gradient-to-br from-sky-50 to-blue-100 flex items-center py-2">
            <SideBar className='max-w-[200px] my-2 ml-2' />
            <div id='dashboard' className='w-full h-full my-2 bg-white/70 rounded-2xl backdrop-blur-xl m-2 p-2 shadow-xl/5 backdrop-saturate-200'>
                {children}
            </div>
        </div>
    )
}

function OpsLayout({ children }) {
    return (
        <div className='fixed dark w-full h-full bg-gradient-to-bl from-sky-800 to-cyan-950 text-white flex items-center justify-center p-2'>
            {children}
        </div>
    )
}

function AuthLayout({ children }) {
    return (
        <div className='fixed w-full h-full bg-gradient-to-br from-pink-50 to-cyan-100 flex justify-center items-center p-2'>
            {children}
        </div>
    )
}

function App() {
    return (
        <UserProvider>
            <Routes>
                {/* Client-end Routing */}
                <Route path="/" element={
                    <ProtectedRoute requiredRole={['USER']}>
                        <ClientLayout>
                            <ClientHome />
                        </ClientLayout>
                    </ProtectedRoute>

                } />
                <Route path="/chart-demo" element={
                    <ClientLayout>
                        <ChartDemo />
                    </ClientLayout>
                } />

                {/* Ops-end Routing */}
                <Route path="/ops" element={
                    <ProtectedRoute requiredRole={['ADMIN', 'SUPERADMIN']}>
                        <OpsLayout>
                            <OpsHome />
                        </OpsLayout>
                    </ProtectedRoute>

                } />

                <Route path="/login" element={
                    <AuthLayout>
                        <Login />
                    </AuthLayout>
                } />

                <Route path='/unauthorized' element={
                    <AuthLayout>
                        <Unauthorized />
                    </AuthLayout>
                } />
            </Routes>
        </UserProvider>

    )
}

export default App
