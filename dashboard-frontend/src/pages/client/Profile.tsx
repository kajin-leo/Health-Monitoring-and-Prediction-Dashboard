import React, { useEffect, useRef, useState } from "react";
import { useForm, Controller } from "react-hook-form";
import { Form, Input, Button, Select, SelectItem } from "@heroui/react";
import { apiClient } from "../../service/axios";
import DashboardCard from "../../components/DashboardCard";
import AvatarUploader from "../../components/Settings/AvatarUploader";
import defaultAvartar from "../../assets/豹豹Idle.svg";
import { userAPI } from "../../service/api";
import { API_CONFIG } from "../../config/api";



type Profile = {
  id: number;
  username: string;
  password?: string | null;
  firstName: string | null;
  lastName: string | null;
  ageYear: number;
  sex: number;
};

const defaultProfile: Profile = {
  id: 0,
  username: "",
  password: undefined,
  firstName: null,
  lastName: null,
  ageYear: 0,
  sex: 0,
};



const Profile: React.FC = () => {
  const [profile, setProfile] = useState<Profile | null>(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [avatarUrl, setAvatarUrl] = useState(defaultAvartar);
  const [isUploading, setIsUploading] = useState(false);
  const [userId, setUserId] = useState(0);

  const mounted = useRef(true);

  const {
    control,
    handleSubmit,
    reset,
    formState: { errors, isDirty },
  } = useForm<Profile>({
    defaultValues: defaultProfile,
    mode: "onBlur",
  });

  const selectedFileRef = useRef<File | null>(null);

  const handleFileSelect = (file: File) => {
  selectedFileRef.current = file; 
  setAvatarUrl(URL.createObjectURL(file)); 
};


  useEffect(() => {
    mounted.current = true;
    const fetchAvatar = async () => {
        try {
            const responseData = await userAPI.getAvatar();
            if (responseData && responseData.id) {
                console.log("id:", responseData.id)
                setUserId(responseData.id);
                if (responseData.avatarUrl) {
                    setAvatarUrl(`${API_CONFIG.BASE_URL}${responseData.avatarUrl}`);
                    console.log(`${API_CONFIG.BASE_URL}${responseData.avatarUrl}`);
                }
            }
        } catch (err) {
            console.error("Failed to load avatar", err);
        } finally {
            setIsUploading(false);
        }
    };
    fetchAvatar();

    


    apiClient.get<Profile>("/static/user_info")
      .then((res) => {
        if (!mounted.current) return;
        console.log("Loaded profile:", res);
        setProfile(res.data);
        reset(res.data);
      })
      .catch((err) => {
        console.error("Load profile error:", err);
        if (!mounted.current) return;
        setProfile(defaultProfile);
        reset(defaultProfile);
      })
      .finally(() => {
        if (mounted.current) setLoading(false);
      });

    return () => {
      mounted.current = false;
    };
  }, [reset]);

  const onSubmit = async (values: Profile) => {

    const payload: Partial<Profile> = {
      username: values.username || undefined,
      firstName: values.firstName || undefined,
      lastName: values.lastName || undefined,
      ageYear: values.ageYear ? Number(values.ageYear) : undefined,
      sex: values.sex !== undefined ? Number(values.sex) : undefined,
    };

    if (values.password && values.password.length > 0) {
      payload.password = values.password;
    }


    setSaving(true);
    try {
      if (selectedFileRef.current) {
          const file = selectedFileRef.current;
          const uploadRes = await userAPI.uploadAvatar(userId, file);
          setAvatarUrl(`${API_CONFIG.BASE_URL}${uploadRes.avatarUrl}`);
        }


      const res = await apiClient.put<Profile>("/static/user_info_update", payload);
      const updated: Profile = {
        ...res.data,
        username: res.data.username ?? "",
        firstName: res.data.firstName ?? "",
        lastName: res.data.lastName ?? "",
        ageYear: res.data.ageYear ?? 0,
        sex: res.data.sex ?? 0,
      };
      setProfile(updated);
      reset(updated);
      alert("Profile saved");
      window.location.reload();

    } catch (err: any) {
      console.error("Save failed:", err);
      const msg = err?.response?.data ? JSON.stringify(err.response.data) : (err.message || "Unknown error");
      alert("Save failed: " + msg);
    } finally {
      setSaving(false);
    }
  };

  if (loading) {
    return (
      <div className="p-6 text-center">
        <div className="animate-spin inline-block w-8 h-8 border-4 border-gray-300 border-t-blue-600 rounded-full" />
        <div className="mt-3">Loading profile…</div>
      </div>
    );
  }


  return (
    <div className="w-full p-4 h-full">
      <DashboardCard className="h-full w-full" noHover={true}>
        <div className="h-full p-4">
          <div className="text-center">
            <h3 className="font-semibold mb-2">Profile picture</h3>
            <AvatarUploader
              currentAvatar={avatarUrl}
              onFileSelect={handleFileSelect}
            />
          </div>
      
          <Form
            aria-label="Profile Form"
            className="w-full flex flex-col gap-4"
            onSubmit={handleSubmit(onSubmit)}
            onReset={() => {
              reset(profile ?? defaultProfile);
            }}
          >
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4 w-full">

            <Controller
              name="username"
              control={control}
              rules={{ required: "Username is required", minLength: { value: 3, message: "Minimum 3 chars" } }}
              render={({ field }) => (
                <Input
                  value={field.value ?? ""}
                  onValueChange={field.onChange}
                  onBlur={field.onBlur}
                  isRequired
                  errorMessage={errors.username?.message as string}
                  label="Username"
                  labelPlacement="outside"
                  placeholder="Enter your username"
                  type="text"
                  className="col-span-1 md:col-span-2 w-full"
                />
              )}
            />

            <Controller
              name="firstName"
              control={control}
              render={({ field }) => (
                <Input
                  {...field}
                  value={field.value ?? ""}
                  errorMessage={errors.firstName?.message as string}
                  label="First Name"
                  labelPlacement="outside"
                  placeholder="Enter your first name"
                  type="text"
                  className="col-span-1 md:col-span-2 w-full"
                />
              )}
            />

            <Controller
              name="lastName"
              control={control}
              render={({ field }) => (
                <Input
                  {...field}
                  value={field.value ?? ""}
                  errorMessage={errors.lastName?.message as string}
                  label="Last Name"
                  labelPlacement="outside"
                  placeholder="Enter your last name"
                  type="text"
                  className="col-span-1 md:col-span-2 w-full"
                />
              )}
            />

            <Controller
              name="password"
              control={control}
              rules={{ minLength: { value: 6, message: "Minimum 6 chars" } }}
              render={({ field }) => (
                <Input
                  value={field.value ?? ""}
                  onValueChange={field.onChange}
                  onBlur={field.onBlur}
                  errorMessage={errors.password?.message as string}
                  label="Password (Minimal 6 characters)"
                  labelPlacement="outside"
                  placeholder="Enter new password"
                  type="password"
                  className="col-span-1 md:col-span-2 w-full"
                />
              )}
            />

            <Controller
              name="ageYear"
              control={control}
              rules={{ min: { value: 9, message: "Age must be >= 9" }, max: { value: 18, message: "Age must be <= 18" } }}
              render={({ field }) => (
                <Input
                  value={field.value !== undefined && field.value !== null ? String(field.value) : ""}
                  onValueChange={field.onChange}
                  onBlur={field.onBlur}
                  errorMessage={errors.ageYear?.message as string}
                  label="Age (Years)"
                  labelPlacement="outside"
                  type="number"
                  min={0}
                  placeholder="e.g. 25"
                  className="col-span-1 w-full"
                />
              )}
            />

            <Controller
              name="sex"
              control={control}
              rules={{ required: "Please select sex" }}
              render={({ field }) => (
                <Select
                  label="Sex"
                  labelPlacement="outside"
                  isRequired
                  placeholder="Select your sex"
                  selectedKeys={
                    field.value === undefined || field.value === null
                      ? new Set([])
                      : new Set([String(field.value)])
                  }
                  onSelectionChange={(keys) => {
                    const key = Array.from(keys as Set<string>)[0];
                    field.onChange(key !== undefined ? Number(key) : null);
                  }}
                  onBlur={field.onBlur}
                  name={field.name}
                  ref={field.ref}
                  errorMessage={errors.sex?.message as string}
                  isInvalid={!!errors.sex}
                  className="col-span-1 w-full"
                >
                  <SelectItem key="1">Male</SelectItem>
                  <SelectItem key="2">Female</SelectItem>
                </Select>
              )}
            />

            <Controller
              name="intakePreference"
              control={control}
              render={({ field }) => (
                <Select
                  label="Intake Preference"
                  labelPlacement="outside"
                  placeholder="Select your intake"
                  selectedKeys={field.value ? new Set([field.value]) : new Set()}
                  onSelectionChange={(keys) => {
                    const key = Array.from(keys as Set<string>)[0];
                    field.onChange(key);
                  }}
                  onBlur={field.onBlur}
                  name={field.name}
                  ref={field.ref}
                  errorMessage={errors.intakePreference?.message as string}
                  className="col-span-1 w-full"
                >
                  <SelectItem key="Balanced">Balanced</SelectItem>
                  <SelectItem key="High-Protein">High-Protein</SelectItem>
                  <SelectItem key="Low-Carb">Low-Carb</SelectItem>
                  <SelectItem key="Vegetarian">Vegetarian</SelectItem>
                </Select>
              )}
            />

            <Controller
              name="movementPreference"
              control={control}
              render={({ field }) => (
                <Select
                  label="Movement Preference"
                  labelPlacement="outside"
                  placeholder="Select your movement"
                  selectedKeys={field.value ? new Set([field.value]) : new Set()}
                  onSelectionChange={(keys) => {
                    const key = Array.from(keys as Set<string>)[0];
                    field.onChange(key);
                  }}
                  onBlur={field.onBlur}
                  name={field.name}
                  ref={field.ref}
                  errorMessage={errors.movementPreference?.message as string}
                  className="col-span-1 w-full"
                >
                  <SelectItem key="Cardio">Cardio</SelectItem>
                  <SelectItem key="Strength">Strength</SelectItem>
                  <SelectItem key="Mixed">Mixed</SelectItem>
                  <SelectItem key="Flexibility">Flexibility</SelectItem>
                </Select>
              )}
            />  
          </div>

          <div className="flex gap-2">
            <Button color="primary" type="submit" disabled={saving}>
              {saving ? "Saving…" : "Save"}
            </Button>
            <Button type="reset" variant="flat">
              Cancel
            </Button>
          </div>
        </Form>
      </div>

      </DashboardCard>
    </div>
  );
};

export default Profile;
