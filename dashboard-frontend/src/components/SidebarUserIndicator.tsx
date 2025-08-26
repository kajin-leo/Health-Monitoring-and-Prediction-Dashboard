import { Avatar, AvatarIcon } from "@heroui/react"

const SideBarUserIndicator = ({ username, userAvatarUrl, ...props }: { username?: string, userAvatarUrl?: string, props?: React.ReactNode }) => {
    
    
    return (
        <div className="w-full h-max-[40px] m-2 flex gap-3 items-end">
            <div className='aspect-square'>
                <Avatar classNames={{
                    base: 'bg-linear-to-b from-sky-700 to-sky-50',
                    icon: 'text-white/80'
                }}
                    icon={<AvatarIcon />} src={userAvatarUrl || ''} />
            </div>
            <div className="w-full h-full flex-col">
                <h2 className="text-black/70 text-sm">
                    Hello,
                </h2>
                <h1 className="text-medium">
                    {username || 'Jane Doe'}
                </h1>
            </div>
        </div>
    );
}

export default SideBarUserIndicator;