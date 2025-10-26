import React, { useId, useRef, useState } from "react";
import { Upload, X } from "lucide-react";

type JsonUploadFieldProps = {
  label?: string;
  placeholder?: string;
  required?: boolean;
  disabled?: boolean;
  helperText?: string;
  errorText?: string;
  onFileSelected?: (file: File | null) => void;
  defaultFile?: File | null;
};

export default function JsonUploadField({
  label = "Upload Exercise Data",
  placeholder = "Select a JSON file",
  required = false,
  disabled = false,
  helperText,
  errorText,
  onFileSelected,
  defaultFile = null,
}: JsonUploadFieldProps) {
  const inputId = useId();
  const fileInputRef = useRef<HTMLInputElement | null>(null);
  const [file, setFile] = useState<File | null>(defaultFile);
  const [dragOver, setDragOver] = useState(false);
  const invalid = Boolean(errorText);

  const pickFile = () => {
    if (disabled) return;
    fileInputRef.current?.click();
  };

  const validateAndSet = (f: File | null) => {
    if (!f) {
      setFile(null);
      onFileSelected?.(null);
      return;
    }
    const isJson = f.type === "application/json" || f.name.toLowerCase().endsWith(".json");
    if (!isJson) {
      alert("Only JSON format supported, please upload .json file");
      setFile(null);
      onFileSelected?.(null);
      return;
    }
    setFile(f);
    onFileSelected?.(f);
  };

  const onInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const f = e.target.files?.[0] || null;
    validateAndSet(f);
  };

  const clearFile = (e: React.MouseEvent) => {
    e.stopPropagation();
    validateAndSet(null);
    if (fileInputRef.current) fileInputRef.current.value = "";
  };

  const onDrop = (e: React.DragEvent) => {
    e.preventDefault();
    setDragOver(false);
    const f = e.dataTransfer.files?.[0] || null;
    validateAndSet(f);
  };

  return (
    <div className="w-90">
      <label
        htmlFor={inputId}
        className="mb-1 block text-sm font-medium text-foreground/80"
      >
        {label}{required && <span className="text-danger ml-0.5">*</span>}
      </label>

      <div
        role="button"
        aria-disabled={disabled}
        aria-invalid={invalid}
        onClick={pickFile}
        onDragOver={(e) => { e.preventDefault(); if (!disabled) setDragOver(true); }}
        onDragLeave={() => setDragOver(false)}
        onDrop={onDrop}
        className={[
          "group relative flex h-12 w-full items-center gap-3 rounded-xl border px-4",
          "transition-all data-[focus=true]:ring-2 data-[focus=true]:ring-primary/40",
          "hover:border-default-300",
          disabled ? "opacity-60 cursor-not-allowed" : "cursor-pointer",
          dragOver ? "border-primary ring-2 ring-primary/30" : "",
          invalid
            ? "border-red-300 bg-red-50"
            : "border-default-200 bg-default-100/60 hover:border-default-300 focus-within:ring-2 focus-within:ring-primary/40",
        ].join(" ")}
        data-focus="false"
        onFocus={(e) => (e.currentTarget.dataset.focus = "true")}
        onBlur={(e) => (e.currentTarget.dataset.focus = "false")}
      >
        <Upload className="h-5 w-5 shrink-0 opacity-70 group-hover:opacity-100" />

        <div className="flex min-w-0 flex-1 items-center">
            <span
                className={`block flex-1 min-w-0 truncate text-sm ${!file ? "text-foreground/50" : "text-foreground"}`}
                title={file ? file.name : placeholder}
            >
                {file ? file.name : placeholder}
            </span>
        </div>

        {file ? (
            <button
            type="button"
            onClick={clearFile}
            className="absolute right-3 top-1/2 -translate-y-1/2 inline-flex items-center gap-1
                        rounded-md px-2 py-1 text-sm text-foreground/70 bg-default-200 hover:bg-default-300"
            tabIndex={0}
            >
            <X className="h-4 w-4" />
            Clear
            </button>
        ) : null}

        <input
          id={inputId}
          ref={fileInputRef}
          type="file"
          accept=".json,application/json"
          className="sr-only"
          onChange={onInputChange}
          disabled={disabled}
          aria-required={required}
        />
      </div>

      {errorText ? (
        <p className="mt-1 text-xs text-danger">{errorText}</p>
      ) : helperText ? (
        <p className="mt-1 text-xs text-foreground/60">{helperText}</p>
      ) : null}
    </div>
  );
}
